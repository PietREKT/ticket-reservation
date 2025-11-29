package org.piet.ticketsbackend.passengers.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PassengerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private LocalDate birthDate;
    private String documentNumber;

    private Long userId;
}
