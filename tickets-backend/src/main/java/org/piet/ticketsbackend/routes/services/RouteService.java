package org.piet.ticketsbackend.routes.services;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.dtos.RouteDto;
import org.piet.ticketsbackend.routes.entities.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

    public Page<Route> getAll(Pageable pageable);
    default public Page<Route> getAll(PaginationDto pagination){
        return getAll(pagination.toPageable());
    }
}
