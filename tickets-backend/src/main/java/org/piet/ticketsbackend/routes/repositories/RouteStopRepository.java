package org.piet.ticketsbackend.routes.repositories;

import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
}
