package org.piet.ticketsbackend.connectionfinder.dtos;

import jakarta.validation.constraints.Max;
import lombok.Value;
import org.piet.ticketsbackend.connectionfinder.models.RouteSearchType;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Value
public class FindConnectionServiceDto implements Serializable {
    Long fromStationId;
    Long toStationId;

    LocalDate date;
    LocalTime time;

    RouteSearchType type;

    @Max(3)
    int maxTransfers;
    int minTransferMinutes;

    PaginationDto pagination;

    public FindConnectionServiceDto(Long fromStationId, Long toStationId, LocalDate date, LocalTime time, RouteSearchType type, Integer maxTransfers, Integer minTransferMinutes, Integer page, Integer size) {
        this.fromStationId = fromStationId;
        this.toStationId = toStationId;
        this.date = date;
        this.time = time;
        this.type = type;
        this.maxTransfers = maxTransfers != null ? maxTransfers : 0;
        this.minTransferMinutes = minTransferMinutes != null ? minTransferMinutes : 10;
        pagination = new PaginationDto(page, size);
    }
}
