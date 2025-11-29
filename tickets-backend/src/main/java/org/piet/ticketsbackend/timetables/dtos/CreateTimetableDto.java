package org.piet.ticketsbackend.timetables.dtos;

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
    LocalTime departureTime;
    Set<DayOfWeek> operatingDays;
    Long routeId;
    String routeName;
    Integer timeAtStation;
}