package org.piet.ticketsbackend.connectionfinder.controllers;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionDto;
import org.piet.ticketsbackend.connectionfinder.dtos.FindConnectionServiceDto;
import org.piet.ticketsbackend.connectionfinder.services.ConnectionFinderService;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.stations.services.StationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.api.prefix}/connections")
@RequiredArgsConstructor
public class ConnectionFinderController {
    private final ConnectionFinderService connectionFinderService;
    private final StationService stationService;


    @GetMapping("/find")
    public ResponseEntity<PageDto<ConnectionDto>> findConnection(FindConnectionServiceDto dto){
        Station from = stationService.getStationById(dto.getFromStationId());
        Station to = stationService.getStationById(dto.getToStationId());
        var connections = connectionFinderService.findConnections(from, to, dto.getDate(), dto.getTime(), dto.getType(), dto.getMaxTransfers(), dto.getMinTransferMinutes(), dto.getPagination());

        return ResponseEntity.ok(PageDto.create(connections));
    }
}
