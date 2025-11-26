package org.piet.ticketsbackend.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketType;

import java.time.LocalDate;

@Data
public class TicketPurchaseRequest {

    @NotNull
    private Long passengerId;

    @NotNull
    private Long routeId;

    @NotNull
    private Long trainId;

    @NotNull
    private Long wagonId;

    @NotNull
    private LocalDate travelDate;

    @NotBlank
    private String startStationCode;

    @NotBlank
    private String endStationCode;

    @NotNull
    private TicketType ticketType;
}
