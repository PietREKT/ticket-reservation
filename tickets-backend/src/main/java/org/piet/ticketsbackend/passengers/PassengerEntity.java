package org.piet.ticketsbackend.passengers;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.piet.ticketsbackend.users.entities.User;

import java.time.LocalDate;

@Entity
@Table(name = "passenger")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
public class PassengerEntity extends User {

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true)
    private String documentNumber;
}
