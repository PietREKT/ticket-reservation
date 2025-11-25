package org.piet.ticketsbackend.passengers;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "passenger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String documentNumber;
}
