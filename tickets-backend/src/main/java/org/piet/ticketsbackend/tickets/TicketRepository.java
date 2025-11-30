package org.piet.ticketsbackend.tickets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Page<TicketEntity> findByPassenger_Id(UUID passengerId, Pageable pageable);

    Optional<TicketEntity> findByTrainIdAndWagonIdAndCoachNumberAndSeatNumberAndTravelDate(
            Long trainId,
            Long wagonId,
            Integer coachNumber,
            Integer seatNumber,
            LocalDate travelDate
    );
}
