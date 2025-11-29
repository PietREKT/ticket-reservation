package org.piet.ticketsbackend.timetables.repositories;

import org.piet.ticketsbackend.timetables.entities.TimetableStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableStopRepository extends JpaRepository<TimetableStop, Long> {
}
