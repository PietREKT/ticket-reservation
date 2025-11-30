package org.piet.ticketsbackend.timetables.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * DTO for {@link org.piet.ticketsbackend.timetables.entities.Timetable}
 */
@Value
public class CreateTimetableDto implements Serializable {
    @NotNull
    LocalTime departureTime;
    @NotEmpty
    Set<DayOfWeek> operatingDays;
    @NotNull
    @Min(0)
    Long routeId;

    String routeName;

    Integer timeAtStation;
}