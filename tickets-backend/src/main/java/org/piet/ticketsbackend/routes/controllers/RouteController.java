package org.piet.ticketsbackend.routes.controllers;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.routes.dtos.CreateRouteDto;
import org.piet.ticketsbackend.routes.dtos.RouteDto;
import org.piet.ticketsbackend.routes.dtos.RouteStopDto;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.services.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @PostMapping("/add")
    public ResponseEntity<RouteDto> createRoute(@RequestBody CreateRouteDto dto){
        Route route = routeService.create(dto.getStops(), dto.getCircular(), dto.getRouteName());
        return ResponseEntity.ok(RouteDto.create(route));
    }

    @GetMapping(params = {"id", "!name"})
    public ResponseEntity<RouteDto> getRouteById(@RequestParam Long id){
        return ResponseEntity.ok(RouteDto.create(routeService.getRouteById(id)));
    }

    @GetMapping(params = {"!id", "name"})
    public ResponseEntity<RouteDto> getRouteByName(@RequestParam String name){
        return ResponseEntity.ok(RouteDto.create(routeService.getRouteByName(name)));
    }

    @GetMapping("/all")
    public ResponseEntity<PageDto<RouteDto>> getAllRoutes(PaginationDto paginationDto){
        var routes = routeService.getAll(paginationDto).map(RouteDto::create);
        return ResponseEntity.ok(PageDto.create(routes));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteById(@PathVariable Long id){
        routeService.deleteRoute(id);
        return ResponseEntity.ok().build();
    }
}
