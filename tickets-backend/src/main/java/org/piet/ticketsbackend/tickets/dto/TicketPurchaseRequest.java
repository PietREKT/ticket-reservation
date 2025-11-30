package org.piet.ticketsbackend.tickets.dto;

import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketType;

import java.time.LocalDate;

@Data
public class TicketPurchaseRequest {

    private Long passengerId;

    private Long trainId;
    private Long wagonId;
    private Integer coachNumber;

    private Long routeId;
    private String startStationCode;
    private String endStationCode;

    private LocalDate travelDate;

    private TicketType ticketType;
}
