package org.piet.ticketsbackend.tickets.client;

import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.tickets.TicketRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.stream.IntStream;

@Component
public class SeatApiClient {

    private final TicketRepository ticketRepository;

    @Value("${app.tickets.maxSeatsPerCoach:80}")
    private int maxSeatsPerCoach;

    public SeatApiClient(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }


    public SeatAllocationResponse allocateSeat(Long trainId,
                                               Long wagonId,
                                               Integer coachNumber,
                                               LocalDate travelDate) {

        int freeSeat = IntStream.rangeClosed(1, maxSeatsPerCoach)
                .filter(seat -> ticketRepository
                        .findByTrainIdAndWagonIdAndCoachNumberAndSeatNumberAndTravelDate(
                                trainId, wagonId, coachNumber, seat, travelDate
                        ).isEmpty())
                .findFirst()
                .orElseThrow(() -> new BadRequestException("No free seats in this coach"));

        String trainName = "Train " + trainId;

        return new SeatAllocationResponse(
                true,
                trainId,
                wagonId,
                coachNumber,
                freeSeat,
                trainName
        );
    }


    public void releaseSeat(Long trainId,
                            Long wagonId,
                            Integer coachNumber,
                            Integer seatNumber,
                            LocalDate travelDate) {

    }

    public static class SeatAllocationResponse {

        private final boolean success;
        private final Long trainId;
        private final Long wagonId;
        private final Integer coachNumber;
        private final Integer seatNumber;
        private final String trainName;

        public SeatAllocationResponse(boolean success,
                                      Long trainId,
                                      Long wagonId,
                                      Integer coachNumber,
                                      Integer seatNumber,
                                      String trainName) {
            this.success = success;
            this.trainId = trainId;
            this.wagonId = wagonId;
            this.coachNumber = coachNumber;
            this.seatNumber = seatNumber;
            this.trainName = trainName;
        }

        public boolean isSuccess() {
            return success;
        }

        public Long getTrainId() {
            return trainId;
        }

        public Long getWagonId() {
            return wagonId;
        }

        public Integer getCoachNumber() {
            return coachNumber;
        }

        public Integer getSeatNumber() {
            return seatNumber;
        }

        public String getTrainName() {
            return trainName;
        }
    }
}
