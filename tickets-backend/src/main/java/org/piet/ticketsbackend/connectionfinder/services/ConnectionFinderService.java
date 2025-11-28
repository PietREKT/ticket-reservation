package org.piet.ticketsbackend.connectionfinder.services;

import org.piet.ticketsbackend.connectionfinder.models.RouteSearchType;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ConnectionFinderService {
    Page<ConnectionDto> findConnections(
            Station from,
            Station to,
            LocalDate date,
            LocalTime time,
            RouteSearchType type,
            int maxTransfers,
            int minTransferMinutes,
            PaginationDto pagination
    );
}
