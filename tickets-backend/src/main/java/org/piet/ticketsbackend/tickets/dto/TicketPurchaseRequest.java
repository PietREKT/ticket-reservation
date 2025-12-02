package org.piet.ticketsbackend.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.piet.ticketsbackend.tickets.TicketType;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TicketPurchaseRequest {

    @NotNull
    private UUID passengerId;

    @NotNull
    private Long trainId;

    @NotNull
    private Long wagonId;

    @NotNull
    private Integer coachNumber;

    @NotNull
    private Long routeId;

    @NotBlank
    private String startStationCode;

    @NotBlank
    private String endStationCode;

    @NotNull
    private LocalDate travelDate;

    @NotNull
    private TicketType ticketType;
}
