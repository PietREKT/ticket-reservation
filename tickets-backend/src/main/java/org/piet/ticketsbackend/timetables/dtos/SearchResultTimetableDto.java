package org.piet.ticketsbackend.timetables.dtos;

import lombok.Value;
import org.piet.ticketsbackend.stations.dtos.StationDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.piet.ticketsbackend.timetables.entities.TimetableStop;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link org.piet.ticketsbackend.timetables.entities.Timetable}
 */
@Value
public class SearchResultTimetableDto implements Serializable {
    Long id;
    List<TimetableStopDto> stops;
    Set<DayOfWeek> operatingDays;
    StationDto station;

    public static SearchResultTimetableDto create(Timetable timetable, Station station, boolean departure) {
        List<TimetableStop> stopsOrdered = timetable.getStops()
                .stream()
                .sorted(Comparator.comparingInt(ts -> ts.getStop().getPosition()))
                .toList();

        int stationIndex = -1;
        for (int i = 0; i < stopsOrdered.size(); i++) {
            var stop = stopsOrdered.get(i);
            if (stop != null && stop.getStop().getStation().equalsStation(station)) {
                stationIndex = i;
                break;
            }
        }

        if (stationIndex < 0)
            throw new IllegalArgumentException();

        List<TimetableStopDto> stopsSliced = (
                departure ?
                        stopsOrdered.subList(stationIndex, stopsOrdered.size())
                        :
                        stopsOrdered.subList(0, stationIndex + 1)
        ).stream()
                .map(TimetableStopDto::create)
                .toList();

        return new SearchResultTimetableDto(
                timetable.getId(),
                stopsSliced,
                timetable.getOperatingDays(),
                StationDto.create(station)
        );
    }
}