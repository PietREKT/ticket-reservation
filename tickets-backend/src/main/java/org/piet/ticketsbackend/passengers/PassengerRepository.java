package org.piet.ticketsbackend.passengers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<PassengerEntity, Long> {
}
