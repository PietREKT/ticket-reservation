package org.piet.ticketsbackend.timetables.services;

import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.dtos.SearchResultTimetableDto;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public interface TimetableService {
    public Timetable getTimetableById(Long id) throws NotFoundException;

    public Page<Timetable> getTimetablesByStation(Station station, PaginationDto dto);

    public Page<Timetable> getTimetablesByStationAndWeekDay(Station station, DayOfWeek dayOfWeek, PaginationDto dto);

    public Page<Timetable> getDeparturesFromStation(Station station, DayOfWeek day, LocalTime notEarlierThan, Pageable pageable);
    default public Page<Timetable> getDeparturesFromStation(Station station, DayOfWeek day, LocalTime notEarlierThan, PaginationDto dto){
        return getDeparturesFromStation(station, day, notEarlierThan, dto.toPageable());
    }
    default public List<Timetable> getDeparturesFromStation(Station station, DayOfWeek day, LocalTime notEarlierThan){
        return getDeparturesFromStation(station, day, notEarlierThan, Pageable.unpaged()).getContent();
    }
    default public Page<SearchResultTimetableDto> getDeparturesAtStationSliced(Station station, DayOfWeek day, LocalTime notLaterThan, PaginationDto dto){
        Page<Timetable> timetables = getDeparturesFromStation(station, day, notLaterThan, dto);
        return timetables.map(t -> SearchResultTimetableDto.create(t, station, true));
    }

    public Page<Timetable> getArrivalsAtStation(Station station, DayOfWeek day, LocalTime notLaterThan, Pageable pageable);
    default public Page<Timetable> getArrivalsAtStation(Station station, DayOfWeek day, LocalTime notLaterThan, PaginationDto dto){
        return getArrivalsAtStation(station, day, notLaterThan, dto.toPageable());
    };
    default public List<Timetable> getArrivalsAtStation(Station station, DayOfWeek day, LocalTime notLaterThan){
        return getArrivalsAtStation(station, day, notLaterThan, Pageable.unpaged()).getContent();
    };
    default public Page<SearchResultTimetableDto> getArrivalsAtStationSliced(Station station, DayOfWeek day, LocalTime notLaterThan, PaginationDto dto){
        Page<Timetable> timetables = getArrivalsAtStation(station, day, notLaterThan, dto);
        return timetables.map(t -> SearchResultTimetableDto.create(t, station, false));
    }

    default public Timetable createTimetable(Route route, Set<DayOfWeek> operatingDays, LocalTime departureTime, Integer timeAtStation){
        return createTimetable(route, operatingDays, departureTime, timeAtStation, null);
    };
    default public Timetable createTimetable(Route route, Set<DayOfWeek> operatingDays, LocalTime departureTime, Double trainSpeed){
        return createTimetable(route, operatingDays, departureTime, null, trainSpeed);
    };
    default public Timetable createTimetable(Route route, Set<DayOfWeek> operatingDays, LocalTime departureTime){
        return createTimetable(route, operatingDays, departureTime, null, null);
    };
    public Timetable createTimetable(Route route, Set<DayOfWeek> operatingDays, LocalTime departureTime, Integer timeAtStation, Double trainSpeed);



    public void deleteTimetableById(Long id);

    default public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, LocalTime newDepartureTime){
        return editTimetable(id, newOperatingDays, newDepartureTime, null, null);
    };
    default public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, Integer newTimeAtStation){
        return editTimetable(id, newOperatingDays, null, newTimeAtStation, null);
    };
    default public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays){
        return editTimetable(id, newOperatingDays, null, null, null);
    };
    default public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, LocalTime newDepartureTime, Double trainSpeed){
        return editTimetable(id, newOperatingDays, newDepartureTime, null, trainSpeed);
    };
    default public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, Integer newTimeAtStation, Double trainSpeed){
        return editTimetable(id, newOperatingDays, null, newTimeAtStation, trainSpeed);
    };
    public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, LocalTime newDepartureTime, Integer newTimeAtStation, Double trainSpeed);
}
