package org.piet.ticketsbackend.timetables.dtos;

import lombok.Value;
import org.piet.ticketsbackend.timetables.entities.Timetable;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link org.piet.ticketsbackend.timetables.entities.Timetable}
 */
@Value
public class TimetableDto implements Serializable {
    Long id;
    List<TimetableStopDto> stops;
    LocalTime departureTime;
    LocalTime arrivalTime;
    Set<DayOfWeek> operatingDays;

    public static TimetableDto create(Timetable timetable){
        return new TimetableDto(
                timetable.getId(),
                timetable.getStops().stream().map(TimetableStopDto::create).toList(),
                timetable.getDepartureTime(),
                timetable.getArrivalTime(),
                timetable.getOperatingDays()
        );
    }
}