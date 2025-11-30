package org.piet.ticketsbackend.tickets.dto;

import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketStatus;
import org.piet.ticketsbackend.tickets.TicketType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TicketResponse {

    private Long id;

    private UUID passengerId;
    private String passengerName;

    private Long trainId;
    private String trainName;
    private Long wagonId;
    private Integer coachNumber;
    private Integer seatNumber;

    private Long routeId;
    private String startStationCode;
    private String endStationCode;
    private String startStationName;
    private String endStationName;

    private LocalDateTime departureTime;
    private LocalDate travelDate;

    private BigDecimal price;
    private TicketType ticketType;
    private TicketStatus status;
}
