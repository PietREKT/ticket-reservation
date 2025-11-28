package org.piet.ticketsbackend.connectionfinder.dtos;

import lombok.Value;
import org.piet.ticketsbackend.stations.dtos.StationDto;
import org.piet.ticketsbackend.stations.entities.Station;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Value
public class ConnectionDto implements Serializable {
    StationDto from;
    StationDto to;
    LocalDateTime departureDateTime, arrivalDateTime;
    List<ConnectionLegDto> legs;
    int transfers;

    public static ConnectionDto create(Station from, Station to, List<ConnectionLegDto> legs) {
        LocalDateTime departure = legs.getFirst().getDepartureDateTime();
        LocalDateTime arrival = legs.getLast().getArrivalDateTime();
        return new ConnectionDto(
                StationDto.create(from),
                StationDto.create(to),
                departure,
                arrival,
                legs,
                Math.max(0, legs.size() - 1)
        );
    }
}
