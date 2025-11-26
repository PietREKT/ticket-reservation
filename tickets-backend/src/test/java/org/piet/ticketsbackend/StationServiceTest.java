package org.piet.ticketsbackend;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.repositories.RouteRepository;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.piet.ticketsbackend.stations.repositories.StationRepository;
import org.piet.ticketsbackend.stations.services.StationServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    @Mock
    private StationRepository stationRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    StationServiceImpl stationService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(stationService, "srid", 4326);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    void getStationById_returnsStation() {
        Station s = new Station();
        s.setId(1L);
        when(stationRepository.findById(1L)).thenReturn(Optional.of(s));

        Station res = stationService.getStationById(1L);

        assertSame(s, res);
    }

    @Test
    void getStationById_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> stationService.getStationById(1L));
    }

    @Test
    void getStationByCode_returnsStation() {
        Station s = new Station();
        s.setCode("WAW");
        when(stationRepository.findByCode("WAW")).thenReturn(Optional.of(s));
        Station res = stationService.getStationByCode("WAW");

        assertSame(s, res);
    }

    @Test
    void getStationByCode_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> stationService.getStationByCode("WAW"));
    }

    @Test
    void getStationsByCity_returnsStations() {
        Pageable pageable = PageRequest.of(0, 10);

        Station a = new Station();
        Station b = new Station();
        a.setId(1L);
        a.setCity("Warszawa");

        b.setId(2L);
        b.setCode("Warszawa");

        when(stationRepository.findByCity("Warszawa", pageable))
                .thenReturn(new PageImpl<>(List.of(a, b), pageable, 2));

        Page<Station> res = stationRepository.findByCity("Warszawa", pageable);

        assertEquals(2, res.getContent().size());
    }

    @Test
    void createStation_withDescription_BuildsStationAndSaves() {
        double x = 20.9808661;
        double y = 52.2240456;

        when(stationRepository.save(any(Station.class)))
                .thenAnswer(inv -> inv.getArgument(0, Station.class));

        Station result = stationService.createStation(
                "WAW",
                "PL",
                "Warszawa",
                x,
                y,
                "Główna"
        );

        verify(stationRepository).save(any(Station.class));

        assertEquals("WAW", result.getCode());
        assertEquals("Warszawa", result.getCity());
        assertEquals("PL", result.getCountryCode());
        assertEquals("Główna", result.getDescription());

        assertNotNull(result.getLocation());
        assertEquals(4326, result.getLocation().getSRID());
        assertEquals(x, result.getLocation().getX(), 1e-6); // x = lon
        assertEquals(y, result.getLocation().getY(), 1e-6); // y = lat
    }

    @Test
    void createStation_withoutDescription_BuildsStationAndSaves() {
        double x = 20.9808661;
        double y = 52.2240456;

        when(stationRepository.save(any(Station.class)))
                .thenAnswer(inv -> inv.getArgument(0, Station.class));

        Station result = stationService.createStation(
                "WAW",
                "PL",
                "Warszawa",
                x,
                y
        );

        verify(stationRepository).save(any(Station.class));

        assertEquals("WAW", result.getCode());
        assertEquals("Warszawa", result.getCity());
        assertEquals("PL", result.getCountryCode());
        assertNull(result.getDescription());

        assertNotNull(result.getLocation());
        assertEquals(4326, result.getLocation().getSRID());
        assertEquals(x, result.getLocation().getX(), 1e-6); // x = lon
        assertEquals(y, result.getLocation().getY(), 1e-6); // y = lat
    }

    @Test
    void createStation_withDescription_throwsDuplicateOnDataIntegrityViolation() {
        when(stationRepository.save(any(Station.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        when(messageSource.getMessage(
                eq("error.stations.duplicate"),
                any(),
                any())
        ).thenReturn("duplicate station");

        DuplicateStationException ex = assertThrows(
                DuplicateStationException.class,
                () -> stationService.createStation(
                        "WAW",
                        "PL",
                        "Warszawa",
                        20.0,
                        50.0,
                        "desc"
                )
        );

        assertEquals("duplicate station", ex.getMessage());
    }

    @Test
    void createStation_withoutDescription_throwsDuplicateOnDataIntegrityViolation() {
        when(stationRepository.save(any(Station.class)))
                .thenThrow(new DataIntegrityViolationException("duplicate"));

        when(messageSource.getMessage(
                eq("error.stations.duplicate"),
                any(),
                any())
        ).thenReturn("duplicate station");

        DuplicateStationException ex = assertThrows(
                DuplicateStationException.class,
                () -> stationService.createStation(
                        "WAW",
                        "PL",
                        "Warszawa",
                        20.0,
                        50.0
                )
        );

        assertEquals("duplicate station", ex.getMessage());
    }

    @Test
    void deleteStation_delegatesToRepository(){
        stationService.deleteStation(10L);

        verify(stationRepository).deleteById(10L);
    }
}
