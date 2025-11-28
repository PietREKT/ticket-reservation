package org.piet.ticketsbackend.wagons.repository;

import org.piet.ticketsbackend.wagons.entity.WagonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WagonRepository extends JpaRepository<WagonEntity, Long> {

    Page<WagonEntity> findAllByTrainId(Long trainId, Pageable pageable);
}
