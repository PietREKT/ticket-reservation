package org.piet.ticketsbackend.tickets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// SOLID: ISP - interfejs zawiera tylko metody potrzebne do obsługi biletów
// SOLID: DIP - warstwa serwisowa zależy od interfejsu, nie od implementacji
public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    // SOLID: SRP - pobranie biletów danego pasażera
    Page<TicketEntity> findByPassengerId(UUID passengerId, Pageable pageable);
}
