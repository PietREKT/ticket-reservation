package org.piet.ticketsbackend.stations.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.stations.dtos.CreateStationDto;
import org.piet.ticketsbackend.stations.dtos.StationDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.stations.services.StationService;
import org.piet.ticketsbackend.timetables.dtos.SearchResultTimetableDto;
import org.piet.ticketsbackend.timetables.dtos.SearchTimetableRequestDto;
import org.piet.ticketsbackend.timetables.dtos.TimetableDto;
import org.piet.ticketsbackend.timetables.services.TimetableService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;
    private final TimetableService timetableService;

    @PostMapping("/add")
    public ResponseEntity<StationDto> createStation(@Valid @RequestBody CreateStationDto dto){
        Station station = stationService.createStation(dto.getCode(), dto.getCountryCode(), dto.getCity(), dto.getX(), dto.getY(), dto.getDescription());
        return ResponseEntity.ok(StationDto.create(station));
    }

    @GetMapping(params = {"code", "!city"})
    public ResponseEntity<StationDto> getStationByCode(@RequestParam("code") String code){
        Station station = stationService.getStationByCode(code);
        return ResponseEntity.ok(StationDto.create(station));
    }

    @GetMapping(params = {"city", "!code"})
    public ResponseEntity<PageDto<StationDto>> getStationsByCity(@RequestParam("city") String city, PaginationDto paginationDto){
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());

        return ResponseEntity.ok(
                PageDto.create(
                        stationService
                                .getStationsByCity(city, pageable)
                                .map(StationDto::create)
                )
        );
    }

    @GetMapping("/{id}/departures")
    public ResponseEntity<PageDto<SearchResultTimetableDto>> getDeparturesFromStation(@PathVariable Long id, SearchTimetableRequestDto dto){
        Station station = stationService.getStationById(id);
        var deps = timetableService.getDeparturesAtStationSliced(station, dto.getDay(), dto.getTime(), dto.getPagination());

        return ResponseEntity.ok(PageDto.create(deps));
    }

    @GetMapping("/{id}/arrivals")
    public ResponseEntity<PageDto<SearchResultTimetableDto>> getArrivalsAtStation(@PathVariable Long id, SearchTimetableRequestDto dto){
        Station station = stationService.getStationById(id);
        var deps = timetableService.getArrivalsAtStationSliced(station, dto.getDay(), dto.getTime(), dto.getPagination());

        return ResponseEntity.ok(PageDto.create(deps));
    }

    @GetMapping("/all")
    public ResponseEntity<PageDto<StationDto>> getAllStations(PaginationDto paginationDto){
        var stations = stationService.getAll(paginationDto).map(StationDto::create);

        return ResponseEntity.ok(PageDto.create(stations));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStationById(@PathVariable Long id){
        stationService.deleteStation(id);
        return ResponseEntity.ok().build();
    }
}
