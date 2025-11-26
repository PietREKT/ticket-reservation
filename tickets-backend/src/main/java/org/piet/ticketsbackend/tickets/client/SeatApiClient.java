package org.piet.ticketsbackend.tickets.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SeatApiClient {

    public SeatAllocation allocateSeat(Long trainId, Long wagonId, LocalDate date) {
        return new SeatAllocation(true, trainId, wagonId, 1, 15, "Mock Train");
    }

    public void releaseSeat(Long trainId, Long wagonId, Integer seat, LocalDate date) {
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SeatAllocation {
        private boolean success;
        private Long trainId;
        private Long wagonId;
        private Integer coachNumber;
        private Integer seatNumber;
        private String trainName;
    }
}
