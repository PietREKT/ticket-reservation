package org.piet.ticketsbackend.wagons.repository;

import org.piet.ticketsbackend.wagons.entity.WagonSeatReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// SOLID: ISP - interfejs zawiera wyłącznie metody związane z rezerwacjami miejsc.
// SOLID: DIP - wykorzystywany przez SeatApiClient jako abstrakcja dostępu do danych.
public interface WagonSeatReservationRepository
        extends JpaRepository<WagonSeatReservationEntity, Long> {

    // SOLID: SRP - pobieranie wszystkich rezerwacji w danym wagonie na daną datę.
    List<WagonSeatReservationEntity> findByWagon_IdAndTravelDate(Long wagonId, LocalDate travelDate);

    // SOLID: SRP - pobieranie konkretnej rezerwacji po wagonie, miejscu i dacie.
    Optional<WagonSeatReservationEntity> findByWagon_IdAndSeatNumberAndTravelDate(
            Long wagonId,
            Integer seatNumber,
            LocalDate travelDate
    );
}
