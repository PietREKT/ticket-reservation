package org.piet.ticketsbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionDto;
import org.piet.ticketsbackend.connectionfinder.models.RouteSearchType;
import org.piet.ticketsbackend.connectionfinder.services.ConnectionFinderServiceImpl;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;
import org.piet.ticketsbackend.timetables.services.TimetableService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConnectionFinderServiceTest {

    @Mock
    MessageSource messageSource;

    @Mock
    TimetableService timetableService;

    @InjectMocks
    ConnectionFinderServiceImpl connectionFinderService;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    void findConnections_throwsBadRequest_whenFromIsNull() {
        Station to = new Station();
        PaginationDto pagination = new PaginationDto(0, 10);

        assertThrows(
                BadRequestException.class,
                () -> connectionFinderService.findConnections(
                        null,
                        to,
                        LocalDate.now(),
                        LocalTime.NOON,
                        RouteSearchType.DEPART_AFTER,
                        0,
                        0,
                        pagination
                )
        );
    }

    @Test
    void findConnections_throwsBadRequest_whenStationsEqual() {
        Station station = new Station();
        station.setId(1L);
        PaginationDto pagination = new PaginationDto(0, 10);

        assertThrows(
                BadRequestException.class,
                () -> connectionFinderService.findConnections(
                        station,
                        station,
                        LocalDate.now(),
                        LocalTime.NOON,
                        RouteSearchType.DEPART_AFTER,
                        0,
                        0,
                        pagination
                )
        );
    }

    @Test
    void findConnections_returnsDirectConnection_forSingleTimetableBetweenTwoStations() {
        // given
        Station from = new Station();
        from.setId(1L);
        Station to = new Station();
        to.setId(2L);
        LocalDate date = LocalDate.of(2025, 1, 8);
        LocalTime requestedDeparture = LocalTime.of(8, 0);

        Timetable timetable = new Timetable();

        TimetableStop fromStop = new TimetableStop();
        fromStop.setTimetable(timetable);
        fromStop.setDepartureTime(LocalTime.of(8, 30));
        try {
            TimetableStop.class.getMethod("setDayOffset", int.class).invoke(fromStop, 0);
        } catch (Exception ignored) {}

        TimetableStop toStop = new TimetableStop();
        toStop.setTimetable(timetable);
        toStop.setArrivalTime(LocalTime.of(9, 0));
        try {
            TimetableStop.class.getMethod("setDayOffset", int.class).invoke(toStop, 0);
        } catch (Exception ignored) {}

        // Route + RouteStops so getStopsInOrder() works
        Route route = new Route();
        RouteStop rsFrom = new RouteStop(0, from, route);
        RouteStop rsTo   = new RouteStop(1, to, route);
        fromStop.setStop(rsFrom);
        toStop.setStop(rsTo);

        timetable.setStops(List.of(fromStop, toStop));

        // only this stub is actually needed
        when(timetableService.getDeparturesFromStation(eq(from), any(), any()))
                .thenReturn(List.of(timetable));

        PaginationDto pagination = new PaginationDto(0, 10);

        // when
        Page<ConnectionDto> page = connectionFinderService.findConnections(
                from,
                to,
                date,
                requestedDeparture,
                RouteSearchType.DEPART_AFTER,
                0,          // no transfers
                5,          // min transfer (irrelevant here)
                pagination
        );

        // then
        assertEquals(1, page.getTotalElements());
        ConnectionDto conn = page.getContent().get(0);

        assertEquals(LocalDateTime.of(date, LocalTime.of(8, 30)), conn.getDepartureDateTime());
        assertEquals(LocalDateTime.of(date, LocalTime.of(9, 0)), conn.getArrivalDateTime());
        assertEquals(1, conn.getLegs().size());
        assertEquals(0, conn.getTransfers());
    }


    @Test
    void arrivalDateTime_appliesDayOffsetToBaseDate() {
        LocalDate baseDate = LocalDate.of(2025, 1, 1);
        TimetableStop stop = new TimetableStop();
        stop.setArrivalTime(LocalTime.of(1, 0));

        try {
            TimetableStop.class.getMethod("setDayOffset", int.class).invoke(stop, 1);
        } catch (Exception e) {
            fail("TimetableStop is expected to have a dayOffset property");
        }

        LocalDateTime result = ReflectionTestUtils.invokeMethod(
                connectionFinderService,
                "arrivalDateTime",
                baseDate,
                stop
        );

        assertEquals(LocalDateTime.of(baseDate.plusDays(1), LocalTime.of(1, 0)), result);
    }

    @Test
    void departureDateTime_doesNotAddExtraDay_whenDwellOnSameDay() {
        LocalDate baseDate = LocalDate.of(2025, 1, 1);
        TimetableStop stop = new TimetableStop();
        stop.setArrivalTime(LocalTime.of(10, 0));
        stop.setDepartureTime(LocalTime.of(10, 5));

        try {
            TimetableStop.class.getMethod("setDayOffset", int.class).invoke(stop, 0);
        } catch (Exception e) {
            fail("TimetableStop is expected to have a dayOffset property");
        }

        LocalDateTime result = ReflectionTestUtils.invokeMethod(
                connectionFinderService,
                "departureDateTime",
                baseDate,
                stop
        );

        assertEquals(LocalDateTime.of(baseDate, LocalTime.of(10, 5)), result);
    }

    @Test
    void departureDateTime_addsExtraDay_whenDwellCrossesMidnight() {
        LocalDate baseDate = LocalDate.of(2025, 1, 1);
        TimetableStop stop = new TimetableStop();
        stop.setArrivalTime(LocalTime.of(23, 55));
        stop.setDepartureTime(LocalTime.of(0, 5));

        try {
            TimetableStop.class.getMethod("setDayOffset", int.class).invoke(stop, 0);
        } catch (Exception e) {
            fail("TimetableStop is expected to have a dayOffset property");
        }

        LocalDateTime result = ReflectionTestUtils.invokeMethod(
                connectionFinderService,
                "departureDateTime",
                baseDate,
                stop
        );

        assertEquals(LocalDateTime.of(baseDate.plusDays(1), LocalTime.of(0, 5)), result);
    }
}
