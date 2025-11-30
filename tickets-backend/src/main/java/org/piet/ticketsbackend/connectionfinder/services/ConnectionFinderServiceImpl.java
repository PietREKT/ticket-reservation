package org.piet.ticketsbackend.connectionfinder.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionLegDto;
import org.piet.ticketsbackend.connectionfinder.models.RouteSearchType;
import org.piet.ticketsbackend.connectionfinder.models.SearchState;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionDto;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;
import org.piet.ticketsbackend.timetables.services.TimetableService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class ConnectionFinderServiceImpl implements ConnectionFinderService {
    private final MessageSource messageSource;
    private final TimetableService timetableService;

    private static final int MAX_INTERNAL_RESULTS = 500;

    @Override
    public Page<ConnectionDto> findConnections(Station from, Station to, LocalDate date, LocalTime time, RouteSearchType type, int maxTransfers, int minTransferMinutes, PaginationDto pagination) {
        if (from == null || to == null) {
            throw new BadRequestException(
                    messageSource.getMessage("error.connections.null_station",
                            new Object[]{},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        if (from.equalsStation(to)) {
            throw new BadRequestException(
                    messageSource.getMessage("error.connections.equal_stations",
                            new Object[]{},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        List<ConnectionDto> all;
        LocalDateTime desiredDateTime = date.atTime(time);

        switch (type) {
            case DEPART_AFTER -> {
                all = findConnectionsByDeparture(from, to, date, time, maxTransfers, minTransferMinutes);
            }
            case ARRIVE_BEFORE -> {
                all = findConnectionsByArrival(from, to, date, time, maxTransfers, minTransferMinutes);
            }
            default -> throw new BadRequestException(
                    messageSource.getMessage("error.connections.no_search_type",
                            new Object[]{},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        List<ConnectionDto> sorted;

        if (type == RouteSearchType.DEPART_AFTER) {
            sorted = all.stream()
                    .sorted(
                            Comparator
                                    .comparing(ConnectionDto::getTransfers)
                                    .thenComparing(ConnectionDto::getDepartureDateTime)
                                    .thenComparing(ConnectionDto::getArrivalDateTime)
                    )
                    .toList();
        } else {
            sorted = all.stream()
                    .filter(c -> !c.getArrivalDateTime().isAfter(desiredDateTime))
                    .sorted(
                            Comparator
                                    .comparing(ConnectionDto::getTransfers)
                                    .thenComparingLong(c ->
                                    Duration.between(c.getArrivalDateTime(), desiredDateTime).toMinutes()
                            )
                    )
                    .toList();
        }

        return PageDto.toPage(sorted, pagination.toPageable());
    }

    private List<ConnectionDto> findConnectionsByDeparture(Station from, Station to, LocalDate date, LocalTime notEarlierThan, int maxTransfers, int minTransferMinutes) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        LocalDateTime startDateTime = LocalDateTime.of(date, notEarlierThan);

        PriorityQueue<SearchState> queue = new PriorityQueue<>(
                Comparator.comparing(SearchState::getArrivalTime)
        );

        Map<Station, Map<Integer, LocalDateTime>> best = new HashMap<>();
        List<ConnectionDto> results = new ArrayList<>();

        queue.add(new SearchState(from, startDateTime, List.of()));

        while (!queue.isEmpty() && results.size() < MAX_INTERNAL_RESULTS) {
            SearchState current = queue.poll();
            Station currentStation = current.getStation();
            LocalDateTime currentArrival = current.getArrivalTime();
            List<ConnectionLegDto> currentLegs = current.getLegs();

            int transfersSoFar = Math.max(0, currentLegs.size() - 1);

            if (isDominated(currentStation, transfersSoFar, currentArrival, best)) continue;

            best.computeIfAbsent(currentStation, s -> new HashMap<>())
                    .put(transfersSoFar, currentArrival);

            if (currentStation.equalsStation(to) && !currentLegs.isEmpty()) {
                ConnectionDto dto = ConnectionDto.create(from, to, currentLegs);
                results.add(dto);
            }

            if (transfersSoFar >= maxTransfers && !currentLegs.isEmpty()) {
                continue;
            }

            LocalDateTime earliestBoardingTime = currentArrival;
            if (!currentLegs.isEmpty()) {
                earliestBoardingTime = earliestBoardingTime.plusMinutes(minTransferMinutes);
            }

            LocalTime searchFromTime = earliestBoardingTime.toLocalTime();

            List<Timetable> candidateTimeTables = timetableService.getDeparturesFromStation(
                    currentStation,
                    dayOfWeek,
                    searchFromTime
            );

            for (var timetable : candidateTimeTables) {
                List<TimetableStop> stopsOrdered = getStopsInOrder(timetable);
                int fromIndex = indexOfStation(stopsOrdered, currentStation);

                if (fromIndex < 0) continue;

                TimetableStop fromStop = stopsOrdered.get(fromIndex);
                LocalDateTime legDeparture = departureDateTime(date, fromStop);

                if (legDeparture.isBefore(earliestBoardingTime)) continue;

                for (int i = fromIndex + 1; i < stopsOrdered.size(); i++) {
                    TimetableStop toStop = stopsOrdered.get(i);
                    LocalDateTime legArrival = arrivalDateTime(date, toStop);

                    if (legArrival.isBefore(legDeparture)) continue;

                    Station nextStation = toStop.getStop().getStation();

                    List<ConnectionLegDto> newLegs = new ArrayList<>(currentLegs);
                    newLegs.add(ConnectionLegDto.create(
                            currentStation,
                            nextStation,
                            legDeparture,
                            legArrival,
                            timetable
                    ));

                    int newTransfers = Math.max(0, newLegs.size() - 1);
                    if (newTransfers > maxTransfers) continue;

                    SearchState nextState = new SearchState(
                            nextStation,
                            legArrival,
                            newLegs
                    );

                    queue.add(nextState);
                }
            }
        }

        return results;
    }

    private List<ConnectionDto> findConnectionsByArrival(Station from, Station to, LocalDate date, LocalTime latestArrival, int maxTransfers, int minTransferMinutes) {
        List<ConnectionDto> all = findConnectionsByDeparture(from, to, date, LocalTime.MIN, maxTransfers, minTransferMinutes);

        return all.stream()
                .filter(c -> !c.getArrivalDateTime().isAfter(date.atTime(latestArrival)))
                .toList();
    }

    private int indexOfStation(List<TimetableStop> stops, Station station) {
        for (int i = 0; i < stops.size(); i++) {
            RouteStop rs = stops.get(i).getStop();
            if (rs != null && rs.getStation().equalsStation(station))
                return i;
        }
        return -1;
    }

    private LocalDateTime arrivalDateTime(LocalDate date, TimetableStop stop) {
        LocalTime time = stop.getArrivalTime();
        if (time == null)
            time = stop.getDepartureTime();
        return LocalDateTime.of(date.plusDays(stop.getDayOffset()), time);
    }

    private LocalDateTime departureDateTime(LocalDate date, TimetableStop stop) {
        LocalTime time = stop.getDepartureTime();
        if (time == null)
            time = stop.getArrivalTime();
        boolean dwelledPastMidnight =
                stop.getDepartureTime() != null &&
                        stop.getArrivalTime() != null &&
                        !stop.getDepartureTime().isAfter(stop.getArrivalTime());
        return LocalDateTime.of(
                date
                        .plusDays(stop.getDayOffset())
                        .plusDays(dwelledPastMidnight ? 1 : 0),
                time);
    }

    private boolean isDominated(
            Station station,
            int transfers,
            LocalDateTime arrivalTime,
            Map<Station, Map<Integer, LocalDateTime>> best
    ) {
        Map<Integer, LocalDateTime> byTransfers = best.get(station);

        if (byTransfers == null) return false;
        LocalDateTime known = byTransfers.get(transfers);
        return known != null && !arrivalTime.isBefore(known);
    }

    private List<TimetableStop> getStopsInOrder(Timetable timetable) {
        return timetable.getStops().stream()
                .sorted(Comparator.comparingInt(t -> t.getStop().getPosition()))
                .toList();
    }
}
