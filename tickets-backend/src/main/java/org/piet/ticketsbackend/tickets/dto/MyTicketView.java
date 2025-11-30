package org.piet.ticketsbackend.tickets.dto;

import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketStatus;
import org.piet.ticketsbackend.tickets.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MyTicketView {

    private Long ticketId;

    private String trainName;
    private Integer coachNumber;
    private Integer seatNumber;

    private String startStationName;
    private String endStationName;

    private LocalDateTime departureTime;

    private BigDecimal price;
    private TicketType ticketType;
    private TicketStatus status;
}
