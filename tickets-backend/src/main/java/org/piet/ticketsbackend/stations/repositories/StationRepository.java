package org.piet.ticketsbackend.stations.repositories;

import org.piet.ticketsbackend.stations.entites.Station;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByCode(String code);

    Page<Station> findByCity(String city, Pageable pageable);

    @Query(value = """
            select ST_DistanceSphere(s1.location, s2.location) / 1000.0
                        from station s1, station s2
                                    where s1.id = :id1 and s2.id = :id2
            """,
    nativeQuery = true
    )
    double getDistanceInKm(@Param("id1") Long id1, @Param("id2") Long id2);
}
