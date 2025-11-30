package org.piet.ticketsbackend.passengers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PassengerRepository extends JpaRepository<PassengerEntity, UUID> {
}
