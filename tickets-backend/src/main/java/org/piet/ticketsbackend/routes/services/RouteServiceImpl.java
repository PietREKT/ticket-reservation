package org.piet.ticketsbackend.routes.services;

import org.piet.ticketsbackend.geo.DistanceService;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.routes.repositories.RouteRepository;
import org.piet.ticketsbackend.routes.repositories.RouteStopRepository;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.repositories.StationRepository;
import org.piet.ticketsbackend.stations.services.StationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private final DistanceService distanceService;
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    private final MessageSource messageSource;

    @Value("${app.trains.avgSpeed}")
    Double avgTrainSpeed;

    public RouteServiceImpl(DistanceService distanceService,
                            RouteRepository routeRepository, StationRepository stationRepository, MessageSource messageSource) {
        this.distanceService = distanceService;
        this.routeRepository = routeRepository;
        this.stationRepository = stationRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Route getRouteById(Long id) throws NotFoundException {
        return routeRepository.findById(id).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("errors.routes.not_found.id",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Override
    public Route getRouteByName(String name) throws NotFoundException {
        return routeRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("errors.routes.not_found.name",
                        new Object[]{name},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Transactional
    public Route create(List<String> routeStops, boolean circular) {
        return create(routeStops, circular, null);
    }

    @Override
    @Transactional
    public Route create(List<String> routeStops, boolean circular, String routeName) {
        Route route = new Route();

        if (routeStops == null || routeStops.isEmpty()) {
            throw new NoSuchElementException("Route must contain at least one stop");
        }

        List<String> allStopCodes = new ArrayList<>(routeStops.size() * (circular ? 2 : 1));
        allStopCodes.addAll(routeStops);

        if (circular && routeStops.size() > 1) {
            for (int i = routeStops.size() - 2; i >= 0; i--) {
                allStopCodes.add(routeStops.get(i));
            }
        }
        List<RouteStop> stops = new ArrayList<>(allStopCodes.size());
        Map<String, Station> stationsByCode = stationRepository
                .findByCodeIn(
                        allStopCodes
                                .stream()
                                .distinct()
                                .toList()
                )
                .stream()
                .collect(Collectors.toMap(Station::getCode, Function.identity()));

        List<String> missing = routeStops
                .stream()
                .filter(s -> !stationsByCode.containsKey(s))
                .distinct()
                .toList();
        if (!missing.isEmpty()) {
            String missingStr = String.join(",", missing);
            throw new NotFoundException(
                    messageSource.getMessage("error.routes.stations.codes",
                            new Object[]{missingStr},
                            LocaleContextHolder.getLocale())
            );
        }

        for (int i = 0; i < allStopCodes.size(); i++) {
            RouteStop stop = new RouteStop();
            stop.setStation(stationsByCode.get(allStopCodes.get(i)));
            stop.setPosition(i);
            stop.setRoute(route);
            stops.add(stop);
        }

        if (routeName == null) {
            if (stops.isEmpty()) {
                throw new NoSuchElementException("Route must contain at least one stop");
            }
            var firstStation = stops.getFirst().getStation();
            var lastStation = stops.get(routeStops.size() - 1).getStation();
            routeName = firstStation.getFullName() + " ->  " + lastStation.getFullName(); // keep double space
        }

        route.setStops(stops);
        route.setName(routeName);
        double len = distanceService.routeLength(route);
        route.setLength(len);

        int timeInMins = (int) Math.ceil((len / avgTrainSpeed) * 60);
        route.setTotalTimeMinutes(timeInMins);

        return routeRepository.save(route);
    }

    private Route editRoute(Route route, String newName, Boolean active) {
        if (newName != null && !route.getName().equalsIgnoreCase(newName)) {
            route.setName(newName);
        }
        if (active != null && route.isActive() != active) {
            route.setActive(!route.isActive());
        }
        return routeRepository.save(route);
    }

    @Override
    public Route editRoute(Long id, String newName, Boolean active) throws NotFoundException {
        Route r = getRouteById(id);
        return editRoute(r, newName, active);
    }

    @Override
    public Route editRoute(String name, String newName, Boolean active) throws NotFoundException {
        Route r = getRouteByName(name);
        return editRoute(r, newName, active);
    }

    @Override
    public void deleteRoute(Long routeId) {
        routeRepository.deleteById(routeId);
    }

}
