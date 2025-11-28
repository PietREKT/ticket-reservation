package org.piet.ticketsbackend.timetables.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.piet.ticketsbackend.routes.dtos.RouteStopDto;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * DTO for {@link org.piet.ticketsbackend.timetables.entities.TimetableStop}
 */
@Value
public class TimetableStopDto implements Serializable {
    Long id;
    @NotNull
    RouteStopDto stop;
    LocalTime arrivalTime;
    LocalTime departureTime;

    public static TimetableStopDto create(TimetableStop timetableStop) {
        return new TimetableStopDto(
                timetableStop.getId(),
                RouteStopDto.create(timetableStop.getStop()),
                timetableStop.getArrivalTime(),
                timetableStop.getDepartureTime()
        );
    }
}