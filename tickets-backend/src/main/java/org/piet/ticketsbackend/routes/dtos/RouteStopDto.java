package org.piet.ticketsbackend.routes.dtos;

import lombok.Value;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.dtos.StationDto;

import java.io.Serializable;

/**
 * DTO for {@link org.piet.ticketsbackend.routes.entities.RouteStop}
 */
@Value
public class RouteStopDto implements Serializable {
    int position;
    StationDto station;

    public static RouteStopDto create(RouteStop stop){
        return new RouteStopDto(
                stop.getPosition(),
                StationDto.create(stop.getStation())
        );
    }
}