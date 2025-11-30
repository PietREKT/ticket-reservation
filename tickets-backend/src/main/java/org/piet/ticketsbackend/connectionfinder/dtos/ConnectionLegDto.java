package org.piet.ticketsbackend.connectionfinder.dtos;

import lombok.Value;
import org.piet.ticketsbackend.stations.dtos.StationDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.dtos.TimetableDto;
import org.piet.ticketsbackend.timetables.entities.Timetable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
public class ConnectionLegDto implements Serializable {
    StationDto from;
    StationDto to;
    LocalDateTime departureDateTime;
    LocalDateTime arrivalDateTime;
    TimetableDto timetable;

    public static ConnectionLegDto create(
            Station from,
            Station to,
            LocalDateTime departure,
            LocalDateTime arrival,
            Timetable timetable
    ){
        return new ConnectionLegDto(
                StationDto.create(from),
                StationDto.create(to),
                departure,
                arrival,
                TimetableDto.create(timetable)
        );
    }
}
