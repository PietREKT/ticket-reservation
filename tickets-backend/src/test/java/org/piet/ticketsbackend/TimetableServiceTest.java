package org.piet.ticketsbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.geo.DistanceService;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;
import org.piet.ticketsbackend.timetables.repositories.TimetableRepository;
import org.piet.ticketsbackend.timetables.services.TimetableServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimetableServiceTest {

    @Mock
    TimetableRepository timetableRepository;

    @Mock
    MessageSource messageSource;

    @Mock
    DistanceService distanceService;

    @InjectMocks
    TimetableServiceImpl timetableService;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        ReflectionTestUtils.setField(timetableService, "defaultTimeAtStation", 5);   // 5 min dwell
        ReflectionTestUtils.setField(timetableService, "defaultTrainSpeed", 80.0);   // 80 km/h
    }

    @Test
    void getTimetableById_returnsTimetable_whenExists() {
        Timetable t = new Timetable();
        t.setId(1L);

        when(timetableRepository.findById(1L)).thenReturn(Optional.of(t));

        Timetable result = timetableService.getTimetableById(1L);

        assertSame(t, result);
    }

    @Test
    void getTimetableById_throwsNotFound_whenMissing() {
        when(timetableRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("errors.timetables.not_found.id"), any(), any()))
                .thenReturn("timetable not found");

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> timetableService.getTimetableById(1L)
        );

        assertEquals("timetable not found", ex.getMessage());
    }

    @Test
    void getTimetablesByStation_delegatesToRepository() {
        Station station = new Station();
        PaginationDto dto = new PaginationDto(0, 10);
        Pageable pageable = dto.toPageable();

        Page<Timetable> page = new PageImpl<>(List.of(new Timetable()));
        when(timetableRepository.findByStops_Stop_Station(station, pageable)).thenReturn(page);

        Page<Timetable> result = timetableService.getTimetablesByStation(station, dto);

        assertSame(page, result);
        verify(timetableRepository).findByStops_Stop_Station(station, pageable);
    }

    @Test
    void getTimetablesByStationAndWeekDay_delegatesToRepository() {
        Station station = new Station();
        DayOfWeek day = DayOfWeek.MONDAY;
        PaginationDto dto = new PaginationDto(1, 5);
        Pageable pageable = dto.toPageable();

        Page<Timetable> page = new PageImpl<>(List.of(new Timetable()));
        when(timetableRepository.findByStationAndDayOfWeek(station, day, pageable)).thenReturn(page);

        Page<Timetable> result = timetableService.getTimetablesByStationAndWeekDay(station, day, dto);

        assertSame(page, result);
        verify(timetableRepository).findByStationAndDayOfWeek(station, day, pageable);
    }

    @Test
    void getDeparturesFromStation_paged_delegatesToRepository() {
        Station station = new Station();
        DayOfWeek day = DayOfWeek.MONDAY;
        LocalTime time = LocalTime.of(8, 0);
        PaginationDto dto = new PaginationDto(0, 20);
        Pageable pageable = dto.toPageable();

        Page<Timetable> page = new PageImpl<>(List.of(new Timetable()));
        when(timetableRepository.findByStationAndDayOfWeekAndDepartureTime(station, day, time, pageable))
                .thenReturn(page);

        Page<Timetable> result = timetableService.getDeparturesFromStation(station, day, time, dto);

        assertSame(page, result);
        verify(timetableRepository).findByStationAndDayOfWeekAndDepartureTime(station, day, time, pageable);
    }

    @Test
    void getDeparturesFromStation_list_delegatesToRepository() {
        Station station = new Station();
        DayOfWeek day = DayOfWeek.MONDAY;
        LocalTime time = LocalTime.of(8, 0);

        List<Timetable> list = List.of(new Timetable(), new Timetable());
        when(timetableRepository.findByStationAndDayOfWeekAndDepartureTime(station, day, time))
                .thenReturn(list);

        List<Timetable> result = timetableService.getDeparturesFromStation(station, day, time);

        assertSame(list, result);
        verify(timetableRepository).findByStationAndDayOfWeekAndDepartureTime(station, day, time);
    }

    @Test
    void getArrivalsAtStation_paged_delegatesToRepository() {
        Station station = new Station();
        DayOfWeek day = DayOfWeek.MONDAY;
        LocalTime time = LocalTime.of(10, 0);
        PaginationDto dto = new PaginationDto(0, 20);
        Pageable pageable = dto.toPageable();

        Page<Timetable> page = new PageImpl<>(List.of(new Timetable()));
        when(timetableRepository.findByStationAndDayOfWeekAndArrivalTime(station, day, time, pageable))
                .thenReturn(page);

        Page<Timetable> result = timetableService.getArrivalsAtStation(station, day, time, dto);

        assertSame(page, result);
        verify(timetableRepository).findByStationAndDayOfWeekAndArrivalTime(station, day, time, pageable);
    }

    @Test
    void getArrivalsAtStation_list_delegatesToRepository() {
        Station station = new Station();
        DayOfWeek day = DayOfWeek.MONDAY;
        LocalTime time = LocalTime.of(10, 0);

        List<Timetable> list = List.of(new Timetable(), new Timetable());
        when(timetableRepository.findByStationAndDayOfWeekAndArrivalTime(station, day, time))
                .thenReturn(list);

        List<Timetable> result = timetableService.getArrivalsAtStation(station, day, time);

        assertSame(list, result);
        verify(timetableRepository).findByStationAndDayOfWeekAndArrivalTime(station, day, time);
    }


    @Test
    void createTimetable_buildsStopsAndTimes_withDefaults() {
        Station a = station("A");
        Station b = station("B");
        Station c = station("C");

        RouteStop rs0 = routeStop(0, a);
        RouteStop rs1 = routeStop(1, b);
        RouteStop rs2 = routeStop(2, c);

        Route route = new Route();
        // nieposortowane, żeby się upewnić, że serwis posortuje
        route.setStops(List.of(rs0, rs2, rs1));

        LocalTime departure = LocalTime.of(8, 0);
        Set<DayOfWeek> days = EnumSet.of(DayOfWeek.MONDAY);

        when(distanceService.timeBetweenStations(any(Station.class), any(Station.class), anyDouble()))
                .thenReturn(10);

        when(timetableRepository.save(any(Timetable.class)))
                .thenAnswer(inv -> inv.getArgument(0, Timetable.class));

        Timetable result = timetableService.createTimetable(route, days, departure, null, null);

        List<TimetableStop> stops = result.getStops();
        assertEquals(3, stops.size());

        assertEquals("A", stops.get(0).getStop().getStation().getCode());
        assertEquals("B", stops.get(1).getStop().getStation().getCode());
        assertEquals("C", stops.get(2).getStop().getStation().getCode());

        // A: dep 08:00
        // B: arr 08:10, dep 08:15
        // C: arr 08:25
        assertNull(stops.get(0).getArrivalTime());
        assertEquals(departure, stops.get(0).getDepartureTime());

        assertEquals(LocalTime.of(8, 10), stops.get(1).getArrivalTime());
        assertEquals(LocalTime.of(8, 15), stops.get(1).getDepartureTime());

        assertEquals(LocalTime.of(8, 25), stops.get(2).getArrivalTime());
        assertNull(stops.get(2).getDepartureTime());

        assertEquals(departure, result.getDepartureTime());
        assertEquals(LocalTime.of(8, 25), result.getArrivalTime());
        assertEquals(days, result.getOperatingDays());
        assertEquals(5, result.getWaitingTimeAtStation()); // default
    }

    @Test
    void createTimetable_usesExplicitTimeAtStation_andTrainSpeed() {
        Station a = station("A");
        Station b = station("B");

        RouteStop rs0 = routeStop(0, a);
        RouteStop rs1 = routeStop(1, b);

        Route route = new Route();
        route.setStops(List.of(rs0, rs1));

        LocalTime departure = LocalTime.of(9, 0);
        Set<DayOfWeek> days = EnumSet.of(DayOfWeek.TUESDAY);

        int customDwell = 7;
        double customSpeed = 100.0;

        when(distanceService.timeBetweenStations(a, b, customSpeed))
                .thenReturn(12); // 12 min travel

        when(timetableRepository.save(any(Timetable.class)))
                .thenAnswer(inv -> inv.getArgument(0, Timetable.class));

        Timetable result = timetableService.createTimetable(
                route, days, departure, customDwell, customSpeed
        );

        List<TimetableStop> stops = result.getStops();
        assertEquals(2, stops.size());

        TimetableStop stop0 = stops.get(0);
        TimetableStop stop1 = stops.get(1);

        assertEquals(departure, stop0.getDepartureTime());
        assertNull(stop0.getArrivalTime());

        assertEquals(LocalTime.of(9, 12), stop1.getArrivalTime());
        assertNull(stop1.getDepartureTime());

        assertEquals(customDwell, result.getWaitingTimeAtStation());
        verify(distanceService).timeBetweenStations(a, b, customSpeed);
    }


    @Test
    void deleteTimetableById_delegatesToRepository() {
        timetableService.deleteTimetableById(42L);
        verify(timetableRepository).deleteById(42L);
    }


    @Test
    void editTimetable_updatesOperatingDaysAndTimes_andArrivalTime() {
        Station a = station("A");
        Station b = station("B");
        Station c = station("C");

        RouteStop rs0 = routeStop(0, a);
        RouteStop rs1 = routeStop(1, b);
        RouteStop rs2 = routeStop(2, c);

        Timetable timetable = new Timetable();
        timetable.setId(1L);
        timetable.setOperatingDays(EnumSet.of(DayOfWeek.MONDAY));
        timetable.setWaitingTimeAtStation(5);
        timetable.setDepartureTime(LocalTime.of(8, 0));

        TimetableStop ts0 = new TimetableStop();
        ts0.setStop(rs0);
        ts0.setTimetable(timetable);
        ts0.setDepartureTime(LocalTime.of(8, 0));

        TimetableStop ts1 = new TimetableStop();
        ts1.setStop(rs1);
        ts1.setTimetable(timetable);
        ts1.setArrivalTime(LocalTime.of(8, 10));
        ts1.setDepartureTime(LocalTime.of(8, 15));

        TimetableStop ts2 = new TimetableStop();
        ts2.setStop(rs2);
        ts2.setTimetable(timetable);
        ts2.setArrivalTime(LocalTime.of(8, 25));

        // znowu celowo nieposortowane
        timetable.setStops(new ArrayList<>(List.of(ts2, ts0, ts1)));

        when(timetableRepository.findById(1L)).thenReturn(Optional.of(timetable));
        when(distanceService.timeBetweenStations(any(Station.class), any(Station.class), anyDouble()))
                .thenReturn(10);

        when(timetableRepository.save(any(Timetable.class)))
                .thenAnswer(inv -> inv.getArgument(0, Timetable.class));

        Set<DayOfWeek> newDays = EnumSet.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY);
        LocalTime newDeparture = LocalTime.of(9, 0);
        int newDwell = 6;
        double newSpeed = 90.0;

        Timetable result = timetableService.editTimetable(
                1L, newDays, newDeparture, newDwell, newSpeed
        );

        assertEquals(newDays, result.getOperatingDays());

        List<TimetableStop> stops = result.getStops().stream()
                .sorted(Comparator.comparingInt(ts -> ts.getStop().getPosition()))
                .toList();

        assertEquals(3, stops.size());

        TimetableStop r0 = stops.get(0);
        TimetableStop r1 = stops.get(1);
        TimetableStop r2 = stops.get(2);

        // A: dep 09:00
        // B: arr 09:10, dep 09:16
        // C: arr 09:26
        assertEquals(LocalTime.of(9, 0), r0.getDepartureTime());
        assertNull(r0.getArrivalTime());

        assertEquals(LocalTime.of(9, 10), r1.getArrivalTime());
        assertEquals(LocalTime.of(9, 16), r1.getDepartureTime());

        assertEquals(LocalTime.of(9, 26), r2.getArrivalTime());
        assertNull(r2.getDepartureTime());

        assertEquals(LocalTime.of(9, 26), result.getArrivalTime());
    }

    @Test
    void editTimetable_onlyOperatingDays_doesNotRecalculateTimes() {
        Timetable timetable = new Timetable();
        timetable.setId(1L);
        timetable.setOperatingDays(EnumSet.of(DayOfWeek.MONDAY));
        timetable.setWaitingTimeAtStation(5);
        timetable.setDepartureTime(LocalTime.of(8, 0));

        TimetableStop ts0 = new TimetableStop();
        ts0.setDepartureTime(LocalTime.of(8, 0));
        TimetableStop ts1 = new TimetableStop();
        ts1.setArrivalTime(LocalTime.of(8, 10));

        timetable.setStops(new ArrayList<>(List.of(ts0, ts1)));

        when(timetableRepository.findById(1L)).thenReturn(Optional.of(timetable));
        when(timetableRepository.save(any(Timetable.class)))
                .thenAnswer(inv -> inv.getArgument(0, Timetable.class));

        Set<DayOfWeek> newDays = EnumSet.of(DayOfWeek.TUESDAY);

        Timetable result = timetableService.editTimetable(
                1L, newDays, null, null, null
        );

        assertEquals(newDays, result.getOperatingDays());
        verify(distanceService, never()).timeBetweenStations(any(), any(), anyDouble());
    }

    private Station station(String code) {
        Station s = new Station();
        s.setCode(code);
        return s;
    }

    private RouteStop routeStop(int position, Station station) {
        RouteStop rs = new RouteStop();
        rs.setPosition(position);
        rs.setStation(station);
        return rs;
    }
}
