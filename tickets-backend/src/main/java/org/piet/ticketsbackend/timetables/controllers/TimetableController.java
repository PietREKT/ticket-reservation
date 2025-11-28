package org.piet.ticketsbackend.timetables.controllers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.services.RouteService;
import org.piet.ticketsbackend.timetables.dtos.CreateTimetableDto;
import org.piet.ticketsbackend.timetables.dtos.EditTimetableDto;
import org.piet.ticketsbackend.timetables.dtos.TimetableDto;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.services.TimetableService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/${app.api.prefix}/timetables")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;
    private final RouteService routeService;

    @PostMapping("/add")
    public ResponseEntity<TimetableDto> createTimetable(@RequestBody CreateTimetableDto dto){
        Route route = routeService.getRouteById(dto.getRouteId());

        Timetable timetable = timetableService.createTimetable(route, dto.getOperatingDays(), dto.getDepartureTime(), dto.getTimeAtStation());

        return ResponseEntity.ok(TimetableDto.create(timetable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimetableDto> getTimetableById(@PathVariable Long id){
        Timetable timetable = timetableService.getTimetableById(id);
        return ResponseEntity.ok(TimetableDto.create(timetable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTimetableById(@PathVariable Long id){
        timetableService.deleteTimetableById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/edit")
    public ResponseEntity<TimetableDto> editTimetable(@RequestBody EditTimetableDto dto){
        Timetable timetable = timetableService.editTimetable(dto.getTimetableId(), dto.getOperatingDays(), dto.getWaitingTimeAtStation());
        return ResponseEntity.ok(TimetableDto.create(timetable));
    }
}
