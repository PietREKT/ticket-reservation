package org.piet.ticketsbackend.passengers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;


import java.time.LocalDate;

@Data
public class PassengerRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private LocalDate birthDate;

    @NotBlank
    private String documentNumber;

    private UUID userId;
}
