package org.piet.ticketsbackend.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByPassengerId(Long passengerId);

    boolean existsByTrainIdAndWagonIdAndSeatNumberAndTravelDateAndStatus(
            Long trainId,
            Long wagonId,
            Integer seatNumber,
            LocalDate travelDate,
            TicketStatus status
    );
}
