package org.piet.ticketsbackend.routes.services;

import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RouteService {
    @Transactional
    public Route create(List<String> routeStops, boolean circular, String routeName);

    public void deleteRoute(Long routeId);

    public Route editRoute(Long id, String newName, Boolean active) throws NotFoundException;

    public Route editRoute(String name, String newName, Boolean active) throws NotFoundException;

    public Route getRouteById(Long id) throws NotFoundException;

    public Route getRouteByName(String name) throws NotFoundException;
}
