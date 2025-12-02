package org.piet.ticketsbackend.tickets.client;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.trains.entity.TrainEntity;
import org.piet.ticketsbackend.wagons.entity.WagonEntity;
import org.piet.ticketsbackend.wagons.entity.WagonSeatReservationEntity;
import org.piet.ticketsbackend.wagons.repository.WagonRepository;
import org.piet.ticketsbackend.wagons.repository.WagonSeatReservationRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SeatApiClient {

    private final WagonRepository wagonRepository;
    private final WagonSeatReservationRepository reservationRepository;

    public record SeatAllocationResponse(
            Long trainId,
            Long wagonId,
            Integer seatNumber,
            LocalDate travelDate,
            String trainName
    ) {
    }

    public SeatAllocationResponse allocateSeat(Long trainId,
                                               Long wagonId,
                                               Integer coachNumber,
                                               LocalDate travelDate) {

        WagonEntity wagon = wagonRepository.findById(wagonId)
                .orElseThrow(() -> new BadRequestException("Wagon not found"));

        TrainEntity train = wagon.getTrain();
        if (train == null || !train.getId().equals(trainId)) {
            throw new BadRequestException("Wagon does not belong to selected train");
        }

        Integer capacity = wagon.getSeatsTotal();
        if (capacity == null || capacity <= 0) {
            throw new BadRequestException("Wagon has no seats configured");
        }

        if (coachNumber == null || coachNumber <= 0) {
            throw new BadRequestException("Coach number must be positive");
        }

        List<WagonSeatReservationEntity> reservations =
                reservationRepository.findByWagon_IdAndTravelDate(wagonId, travelDate);

        Set<Integer> taken = new HashSet<>();
        for (WagonSeatReservationEntity r : reservations) {
            taken.add(r.getSeatNumber());
        }

        Integer freeSeat = null;
        for (int seat = 1; seat <= capacity; seat++) {
            if (!taken.contains(seat)) {
                freeSeat = seat;
                break;
            }
        }

        if (freeSeat == null) {
            throw new BadRequestException("No free seats in this wagon for selected date");
        }

        WagonSeatReservationEntity reservation = new WagonSeatReservationEntity();
        reservation.setWagon(wagon);
        reservation.setSeatNumber(freeSeat);
        reservation.setTravelDate(travelDate);
        reservationRepository.save(reservation);

        String trainName = train.getModel();

        return new SeatAllocationResponse(
                trainId,
                wagonId,
                freeSeat,
                travelDate,
                trainName
        );
    }

    public void releaseSeat(Long trainId,
                            Long wagonId,
                            Integer seatNumber,
                            LocalDate travelDate) {

        WagonEntity wagon = wagonRepository.findById(wagonId)
                .orElseThrow(() -> new BadRequestException("Wagon not found"));

        TrainEntity train = wagon.getTrain();
        if (train == null || !train.getId().equals(trainId)) {
            throw new BadRequestException("Wagon does not belong to selected train");
        }

        reservationRepository
                .findByWagon_IdAndSeatNumberAndTravelDate(wagonId, seatNumber, travelDate)
                .ifPresent(reservationRepository::delete);
    }
}
