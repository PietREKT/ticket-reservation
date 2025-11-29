package org.piet.ticketsbackend.routes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.piet.ticketsbackend.routes.entities.Route;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link org.piet.ticketsbackend.routes.entities.Route}
 */
@Value
public class RouteDto implements Serializable {
    Long id;
    List<RouteStopDto> stops;
    @NotBlank
    String name;
    Double length;
    Integer totalTimeMinutes;
    boolean active;

    public static RouteDto create(Route route){
        return new RouteDto(
                route.getId(),
                route.getStops().stream().map(RouteStopDto::create).toList(),
                route.getName(),
                route.getLength(),
                route.getTotalTimeMinutes(),
                route.isActive()
        );
    }
}