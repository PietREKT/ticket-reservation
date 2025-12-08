package org.piet.ticketsbackend.passengers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

// SOLID: ISP/DIP -  interfejs repozytorium dla PassengerEntity
public interface PassengerRepository extends JpaRepository<PassengerEntity, UUID> {
}
