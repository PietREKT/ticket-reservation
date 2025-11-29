package org.piet.ticketsbackend.timetables.dtos;

import lombok.Value;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Value
public class SearchTimetableRequestDto implements Serializable {
    DayOfWeek day;
    LocalTime time;
    PaginationDto pagination;

    public SearchTimetableRequestDto(DayOfWeek day, LocalTime time, Integer page, Integer size) {
        this.day = day;
        this.time = time;
        pagination = new PaginationDto(page, size);
    }
}
