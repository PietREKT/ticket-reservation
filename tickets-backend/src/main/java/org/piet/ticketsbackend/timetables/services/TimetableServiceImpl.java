package org.piet.ticketsbackend.timetables.services;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.geo.DistanceService;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;
import org.piet.ticketsbackend.timetables.repositories.TimetableRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {

    private final TimetableRepository timetableRepository;
    private final MessageSource messageSource;
    private final DistanceService distanceService;

    @Value("${app.stations.defaultWaitTime}")
    private Integer defaultTimeAtStation;

    @Value("${app.trains.avgSpeed}")
    private Double defaultTrainSpeed;

    @Override
    public Timetable getTimetableById(Long id) throws NotFoundException {
        return timetableRepository.findById(id).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("errors.timetables.not_found.id",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                ))
        );
    }

    @Override
    public Page<Timetable> getTimetablesByStation(Station station, PaginationDto dto) {
        return timetableRepository.findByStops_Stop_Station(station, dto.toPageable());
    }

    @Override
    public Page<Timetable> getTimetablesByStationAndWeekDay(Station station, DayOfWeek dayOfWeek, PaginationDto dto) {
        return timetableRepository.findByStationAndDayOfWeek(station, dayOfWeek, dto.toPageable());
    }

    @Override
    public Page<Timetable> getDeparturesFromStation(Station station, DayOfWeek day, LocalTime notEarlierThan, Pageable pageable) {
        return timetableRepository.findByStationAndDayOfWeekAndDepartureTime(station, day, notEarlierThan, pageable);
    }

    @Override
    public Page<Timetable> getArrivalsAtStation(Station station, DayOfWeek day, LocalTime notLaterThan, Pageable pageable) {
        return timetableRepository.findByStationAndDayOfWeekAndArrivalTime(station, day, notLaterThan, pageable);
    }

    @Override
    public Timetable createTimetable(Route route, Set<DayOfWeek> operatingDays, LocalTime departureTime, Integer timeAtStation, Double trainSpeed) {
        Timetable timetable = new Timetable();
        List<RouteStop> routeStops = route.getStops()
                .stream()
                .sorted(Comparator.comparingInt(RouteStop::getPosition))
                .toList();
        List<TimetableStop> timetableStops = new ArrayList<>();

        timeAtStation = timeAtStation != null ? timeAtStation : defaultTimeAtStation;
        trainSpeed = trainSpeed != null ? trainSpeed : defaultTrainSpeed;
        int dayOffset = 0;
        for (int i = 0; i < routeStops.size(); i++) {
            TimetableStop stop = new TimetableStop();

            stop.setStop(routeStops.get(i));
            if (i == 0) {
                stop.setDepartureTime(departureTime);
            } else {
                TimetableStop previousStop = timetableStops.get(i - 1);
                stop.setArrivalTime(
                        previousStop.getDepartureTime().plusMinutes(
                                distanceService.timeBetweenStations(routeStops.get(i - 1).getStation(), routeStops.get(i).getStation(), trainSpeed)
                        )
                );
                if (!stop.getArrivalTime().isAfter(previousStop.getDepartureTime())){
                    dayOffset++;
                    stop.setDayOffset(dayOffset);
                }
                if (i < routeStops.size() - 1) {
                    stop.setDepartureTime(stop.getArrivalTime().plusMinutes((long) timeAtStation));
                }
            }
            stop.setTimetable(timetable);
            timetableStops.add(stop);
        }

        timetable.setDepartureTime(departureTime);
        timetable.setArrivalTime(timetableStops.getLast().getArrivalTime());
        timetable.setOperatingDays(operatingDays);
        timetable.setStops(timetableStops);
        timetable.setWaitingTimeAtStation(timeAtStation);
        return timetableRepository.save(timetable);
    }

    @Override
    public void deleteTimetableById(Long id) {
        timetableRepository.deleteById(id);
    }

    @Override
    public Timetable editTimetable(Long id, Set<DayOfWeek> newOperatingDays, LocalTime newDepartureTime, Integer newTimeAtStation, Double trainSpeed) {
        Timetable timetable = getTimetableById(id);
        trainSpeed = trainSpeed != null ? trainSpeed : defaultTrainSpeed;
        if (newOperatingDays != null &&
                !newOperatingDays.isEmpty() &&
                !newOperatingDays.equals(timetable.getOperatingDays())) {
            timetable.setOperatingDays(newOperatingDays);
        }
        if ((newDepartureTime != null && !newDepartureTime.equals(timetable.getDepartureTime()))
                || (newTimeAtStation != null && !newTimeAtStation.equals(timetable.getWaitingTimeAtStation())) ) {
            Integer timeAtStation = newTimeAtStation != null ? newTimeAtStation : timetable.getWaitingTimeAtStation();
            LocalTime departureTime = newDepartureTime != null ? newDepartureTime : timetable.getDepartureTime();
            List<TimetableStop> stops = timetable.getStops()
                    .stream()
                    .sorted(
                            Comparator.comparingInt(st -> st.getStop().getPosition())
                    )
                    .toList();
            for (int i = 0; i < stops.size(); i++) {
                TimetableStop stop = stops.get(i);
                if (i == 0) {
                    stop.setDepartureTime(departureTime);
                } else {
                    TimetableStop previous = stops.get(i - 1);
                    stop.setArrivalTime(
                            previous.getDepartureTime().plusMinutes(
                                    distanceService.timeBetweenStations(previous.getStop().getStation(), stop.getStop().getStation(), trainSpeed)
                            )
                    );
                    if (i < stops.size() - 1) {
                        stop.setDepartureTime(stop.getArrivalTime().plusMinutes(timeAtStation));
                    }
                }
            }
            timetable.setArrivalTime(stops.getLast().getArrivalTime());
        }
        return timetableRepository.save(timetable);
    }
}
