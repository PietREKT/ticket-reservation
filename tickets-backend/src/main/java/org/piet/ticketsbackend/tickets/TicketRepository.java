package org.piet.ticketsbackend.tickets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Page<TicketEntity> findByPassenger_Id(UUID passengerId, Pageable pageable);
}
