package org.piet.ticketsbackend.stations.controllers;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.stations.dtos.CreateStationDto;
import org.piet.ticketsbackend.stations.dtos.StationDto;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.services.StationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/stations")
public class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @PostMapping("/add")
    public ResponseEntity<StationDto> createStation(@RequestBody CreateStationDto dto){
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
}
