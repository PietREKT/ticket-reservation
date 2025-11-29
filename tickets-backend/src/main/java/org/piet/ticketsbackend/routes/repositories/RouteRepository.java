package org.piet.ticketsbackend.routes.repositories;

import org.piet.ticketsbackend.routes.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByNameIgnoreCase(String name);
}
