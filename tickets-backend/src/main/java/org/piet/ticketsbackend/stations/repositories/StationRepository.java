package org.piet.ticketsbackend.stations.repositories;

import org.piet.ticketsbackend.stations.entites.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByCode(String code);

    Page<Station> findByCity(String city, Pageable pageable);

    List<Station> findByCodeIn(Collection<String> codes);
}
