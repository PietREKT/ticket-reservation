package org.piet.ticketsbackend.passengers;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

// SOLID: SRP
@Entity
@Table(name = "passenger")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String documentNumber;
}
