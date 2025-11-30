package org.piet.ticketsbackend.passengers.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;


@Data
public class PassengerResponse {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private String documentNumber;
}
