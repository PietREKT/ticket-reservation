package org.piet.ticketsbackend.passengers.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PassengerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String documentNumber;
    private LocalDate birthDate;
}
