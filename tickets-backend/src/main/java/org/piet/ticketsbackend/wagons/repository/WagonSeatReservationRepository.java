package org.piet.ticketsbackend.wagons.repository;

import org.piet.ticketsbackend.wagons.entity.WagonSeatReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WagonSeatReservationRepository extends JpaRepository<WagonSeatReservationEntity, Long> {

    List<WagonSeatReservationEntity> findByWagon_IdAndTravelDate(Long wagonId, LocalDate travelDate);

    Optional<WagonSeatReservationEntity> findByWagon_IdAndSeatNumberAndTravelDate(
            Long wagonId, Integer seatNumber, LocalDate travelDate
    );
}
