package org.piet.ticketsbackend.tickets.dto;

import lombok.Builder;
import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketStatus;
import org.piet.ticketsbackend.tickets.TicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TicketResponse {

    private Long id;
    private Long passengerId;
    private String passengerName;

    private Long trainId;
    private String trainName;
    private Integer coachNumber;
    private Integer seatNumber;

    private String startStation;
    private String endStation;

    private LocalDateTime departureTime;
    private LocalDate travelDate;

    private BigDecimal price;
    private TicketType ticketType;
    private TicketStatus status;
}
