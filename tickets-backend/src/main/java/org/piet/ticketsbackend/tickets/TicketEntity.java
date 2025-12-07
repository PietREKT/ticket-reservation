package org.piet.ticketsbackend.tickets;

import jakarta.persistence.*;
import lombok.*;
import org.piet.ticketsbackend.passengers.PassengerEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

// SOLID: SRP - encja reprezentuje wyłącznie strukturę danych tabeli
// SOLID: OCP - można rozszerzać przez dziedziczenie bez modyfikacji
@Entity
@Table(name = "ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SOLID: LSP - relacja ManyToOne z PassengerEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passenger;

    private Long trainId;
    private String trainName;
    private Long wagonId;
    private Integer coachNumber;
    private Integer seatNumber;
    private Long routeId;
    private String startStationCode;
    private String endStationCode;
    private String startStationName;
    private String endStationName;
    private LocalDateTime departureTime;
    private LocalDate travelDate;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
