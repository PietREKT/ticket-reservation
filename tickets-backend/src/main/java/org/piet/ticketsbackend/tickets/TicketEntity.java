package org.piet.ticketsbackend.tickets;

import jakarta.persistence.*;
import lombok.*;
import org.piet.ticketsbackend.passengers.PassengerEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private PassengerEntity passenger;

    // powiązanie z pociągiem
    private Long trainId;
    private String trainName;

    // powiązanie z wagonem
    private Long wagonId;         // klucz do WagonEntity
    private Integer coachNumber;  // np. sekcja w wagonie
    private Integer seatNumber;   // konkretne miejsce w wagonie

    // trasa
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
