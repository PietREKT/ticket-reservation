package org.piet.ticketsbackend.tickets;

import jakarta.persistence.*;
import lombok.*;
import org.piet.ticketsbackend.passengers.PassengerEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private PassengerEntity passenger;

    private Long wagonId;
    private Long trainId;

    private String startStation;
    private String endStation;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private Integer coachNumber;
    private String trainName;
    private Integer seatNumber;

    private LocalDateTime departureTime;
    private LocalDate travelDate;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;
}
