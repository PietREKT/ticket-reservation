package org.piet.ticketsbackend.trains.repository;

import org.piet.ticketsbackend.trains.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<TrainEntity, Long> {
}
