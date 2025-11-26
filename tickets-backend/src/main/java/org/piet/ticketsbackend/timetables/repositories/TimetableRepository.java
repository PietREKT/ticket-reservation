package org.piet.ticketsbackend.timetables.repositories;

import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.timetables.entities.Timetable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    Page<Timetable> findByStops_Stop_Station(Station station, Pageable pageable);

    @Query("""
            select distinct t
                        from Timetable t
                                    join t.stops ts
                                                where ts.stop.station = :station
                                                            and :day member of t.operatingDays
            """)
    Page<Timetable> findByStationAndDayOfWeek(@Param("station") Station station, @Param("day") DayOfWeek day, Pageable pageable);


    default List<Timetable> findByStationAndDayOfWeekAndDepartureTime(Station station, DayOfWeek day, LocalTime departureTime){
        return findByStationAndDayOfWeekAndDepartureTime(station, day, departureTime, Pageable.unpaged()).getContent();
    };

    @Query("""
            select distinct t
                        from Timetable t
                                    join t.stops ts
                                                where ts.stop.station = :station
                                                            and :day member of t.operatingDays
                                                                        and ts.departureTime >= :departure_time
            """)
    Page<Timetable> findByStationAndDayOfWeekAndDepartureTime(@Param("station") Station station, @Param("day") DayOfWeek day, @Param("departure_time") LocalTime departureTime, Pageable pageable);

    @Query("""
            select distinct t
                        from Timetable t
                                    join t.stops ts
                                                where ts.stop.station = :station
                                                            and :day member of t.operatingDays
                                                                        and ts.arrivalTime <= :arrival_time
            """)
    Page<Timetable> findByStationAndDayOfWeekAndArrivalTime(@Param("station") Station station, @Param("day") DayOfWeek day, @Param("arrival_time") LocalTime arrivalTime, Pageable pageable);

    default List<Timetable> findByStationAndDayOfWeekAndArrivalTime(Station station, DayOfWeek day, LocalTime arrivalTime){
        return findByStationAndDayOfWeekAndArrivalTime(station, day, arrivalTime, Pageable.unpaged()).getContent();
    }
}
