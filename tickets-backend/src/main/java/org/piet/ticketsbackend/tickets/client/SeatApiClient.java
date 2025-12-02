package org.piet.ticketsbackend.tickets.client;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.wagons.entity.WagonEntity;
import org.piet.ticketsbackend.wagons.repository.WagonRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SeatApiClient {

    private final WagonRepository wagonRepository;

    /**
     * Allocation result
     */
    public record SeatAllocationResponse(
            Long trainId,
            Long wagonId,
            Integer seatNumber,
            LocalDate travelDate
    ) {}

    /**
     * Allocate seat
     */
    public SeatAllocationResponse allocateSeat(Long trainId,
                                               Long wagonId,
                                               Integer coachNumber,
                                               LocalDate travelDate) {

        WagonEntity wagon = wagonRepository.findById(wagonId)
                .orElseThrow(() -> new BadRequestException("Wagon not found"));

        if (wagon.getTrain() == null || !wagon.getTrain().getId().equals(trainId)) {
            throw new BadRequestException("Wagon does not belong to selected train");
        }

        Integer capacity = wagon.getSeatsTotal();
        if (capacity == null || capacity <= 0) {
            throw new BadRequestException("Wagon has no seats configured");
        }

        // coachNumber: numer wagonu w składzie – tylko walidujemy, że jest dodatni
        if (coachNumber <= 0) {
            throw new BadRequestException("Coach number must be positive");
        }

        // losujemy MIEJSCE w wagonie 1..capacity
        int seatNumber = 1 + (int) (Math.random() * capacity);

        return new SeatAllocationResponse(trainId, wagonId, seatNumber, travelDate);
    }


    /**
     * Release seat after cancellation
     */
    public void releaseSeat(Long trainId,
                            Long wagonId,
                            Integer coachNumber,
                            LocalDate travelDate) {

        WagonEntity wagon = wagonRepository.findById(wagonId)
                .orElseThrow(() -> new BadRequestException("Wagon not found"));

        if (wagon.getTrain() == null || !wagon.getTrain().getId().equals(trainId)) {
            throw new BadRequestException("Wagon does not belong to selected train");
        }

        // REAL implementation would restore seat availability here
        System.out.println("Seat released: train=" + trainId +
                ", wagon=" + wagonId +
                ", seat=" + coachNumber +
                ", date=" + travelDate);
    }
}
