package org.piet.ticketsbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.piet.ticketsbackend.geo.DistanceService;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.repositories.RouteRepository;
import org.piet.ticketsbackend.routes.services.RouteServiceImpl;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.stations.repositories.StationRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    DistanceService distanceService;

    @Mock
    RouteRepository routeRepository;

    @Mock
    StationRepository stationRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    RouteServiceImpl routeService;

    @BeforeEach
    void setUp() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }


    @Test
    void getRouteById_returnsRoute() {
        Route r = new Route();
        r.setId(1L);
        when(routeRepository.findById(1L)).thenReturn(Optional.of(r));

        Route result = routeService.getRouteById(1L);

        assertSame(r, result);
    }

    @Test
    void getRouteById_throwsNotFound() {
        when(routeRepository.findById(1L)).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("errors.routes.not_found.id"), any(), any()))
                .thenReturn("route not found");

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> routeService.getRouteById(1L)
        );
        assertEquals("route not found", ex.getMessage());
    }

    @Test
    void getRouteByName_returnsRoute() {
        Route r = new Route();
        r.setName("R1");
        when(routeRepository.findByNameIgnoreCase("R1")).thenReturn(Optional.of(r));

        Route result = routeService.getRouteByName("R1");

        assertSame(r, result);
    }

    @Test
    void getRouteByName_throwsNotFound() {
        when(routeRepository.findByNameIgnoreCase("R1")).thenReturn(Optional.empty());
        when(messageSource.getMessage(eq("errors.routes.not_found.name"), any(), any()))
                .thenReturn("route name not found");

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> routeService.getRouteByName("R1")
        );
        assertEquals("route name not found", ex.getMessage());
    }


    @Test
    void create_nonCircular_buildsStopsAndCalculatesLengthAndTime() {
        List<String> codes = List.of("A", "B", "C");

        Station a = station("A", "Station A");
        Station b = station("B", "Station B");
        Station c = station("C", "Station C");

        when(stationRepository.findByCodeIn(any()))
                .thenReturn(List.of(a, b, c));

        when(distanceService.routeLength(any(Route.class))).thenReturn(100.0);

        when(distanceService.travelTimeInMinutes(any(Route.class))).thenReturn(75);

        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route route = routeService.create(codes, false, null);


        assertEquals(3, route.getStops().size());
        assertEquals("A", route.getStops().get(0).getStation().getCode());
        assertEquals(0, route.getStops().get(0).getPosition());
        assertEquals("B", route.getStops().get(1).getStation().getCode());
        assertEquals(1, route.getStops().get(1).getPosition());
        assertEquals("C", route.getStops().get(2).getStation().getCode());
        assertEquals(2, route.getStops().get(2).getPosition());

        assertEquals("Station A ->  Station C", route.getName());

        assertEquals(100.0, route.getLength());

        assertEquals(75, route.getTotalTimeMinutes());

        verify(distanceService).routeLength(any(Route.class));
        verify(routeRepository).save(any(Route.class));
    }

    @Test
    void create_circular_buildsMirroredStops() {
        List<String> codes = List.of("A", "B", "C");

        Station a = station("A", "Station A");
        Station b = station("B", "Station B");
        Station c = station("C", "Station C");

        when(stationRepository.findByCodeIn(any()))
                .thenReturn(List.of(a, b, c));

        when(distanceService.routeLength(any(Route.class))).thenReturn(50.0);
        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route route = routeService.create(codes, true, null);

        assertEquals(5, route.getStops().size());
        assertEquals("A", route.getStops().get(0).getStation().getCode());
        assertEquals("B", route.getStops().get(1).getStation().getCode());
        assertEquals("C", route.getStops().get(2).getStation().getCode());
        assertEquals("B", route.getStops().get(3).getStation().getCode());
        assertEquals("A", route.getStops().get(4).getStation().getCode());

        for (int i = 0; i < 5; i++) {
            assertEquals(i, route.getStops().get(i).getPosition());
        }

        assertEquals("Station A ->  Station C", route.getName());
    }

    @Test
    void create_usesProvidedRouteName_whenNotNull() {
        List<String> codes = List.of("A", "B");

        Station a = station("A", "Station A");
        Station b = station("B", "Station B");

        when(stationRepository.findByCodeIn(any()))
                .thenReturn(List.of(a, b));

        when(distanceService.routeLength(any(Route.class))).thenReturn(10.0);
        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route route = routeService.create(codes, false, "Custom name");

        assertEquals("Custom name", route.getName());
    }

    @Test
    void create_throwsWhenRouteStopsEmpty() {
        assertThrows(
                NoSuchElementException.class,
                () -> routeService.create(List.of(), false, null)
        );
    }

    @Test
    void create_throwsNotFoundWhenStationMissing() {
        List<String> codes = List.of("A", "B");

        Station a = station("A", "Station A");
        when(stationRepository.findByCodeIn(any()))
                .thenReturn(List.of(a));

        when(messageSource.getMessage(eq("error.routes.stations.codes"), any(), any()))
                .thenReturn("missing stations");

        NotFoundException ex = assertThrows(
                NotFoundException.class,
                () -> routeService.create(codes, false, null)
        );

        assertEquals("missing stations", ex.getMessage());
    }

    @Test
    void editRouteById_updatesNameAndActive() {
        Route route = new Route();
        route.setId(1L);
        route.setName("Old name");
        route.setActive(true);

        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route result = routeService.editRoute(1L, "New name", false);

        assertEquals("New name", result.getName());
        assertFalse(result.isActive());
        verify(routeRepository).save(route);
    }

    @Test
    void editRouteByName_updatesNameAndActive() {
        Route route = new Route();
        route.setName("Route1");
        route.setActive(false);

        when(routeRepository.findByNameIgnoreCase("Route1"))
                .thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route result = routeService.editRoute("Route1", "Route2", true);

        assertEquals("Route2", result.getName());
        assertTrue(result.isActive());
        verify(routeRepository).save(route);
    }

    @Test
    void editRoute_doesNotChangeWhenParamsNull() {
        Route route = new Route();
        route.setId(1L);
        route.setName("Name");
        route.setActive(true);

        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(routeRepository.save(any(Route.class)))
                .thenAnswer(inv -> inv.getArgument(0, Route.class));

        Route result = routeService.editRoute(1L, null, null);

        assertEquals("Name", result.getName());
        assertTrue(result.isActive());
    }


    @Test
    void deleteRoute_delegatesToRepository() {
        routeService.deleteRoute(10L);

        verify(routeRepository).deleteById(10L);
    }

    private Station station(String code, String fullName) {
        Station s = new Station() {
            @Override
            public String getFullName() {
                return fullName;
            }
        };
        s.setCode(code);
        return s;
    }
}
