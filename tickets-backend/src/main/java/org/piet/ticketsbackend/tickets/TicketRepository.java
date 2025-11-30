package org.piet.ticketsbackend.tickets;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    List<TicketEntity> findByPassenger_Id(Long passengerId);
}
