package org.piet.ticketsbackend.passengers.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

// SOLID: SRP
@Data
public class PassengerResponse {

    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String documentNumber;
}
