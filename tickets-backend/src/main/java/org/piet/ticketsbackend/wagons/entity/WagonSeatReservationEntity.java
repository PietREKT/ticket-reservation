package org.piet.ticketsbackend.wagons.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "wagon_seat_reservation",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"wagon_id", "seat_number", "travel_date"}
        )
)
public class WagonSeatReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wagon_id", nullable = false)
    private WagonEntity wagon;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "travel_date", nullable = false)
    private LocalDate travelDate;

    public Long getId() {
        return id;
    }

    public WagonEntity getWagon() {
        return wagon;
    }

    public void setWagon(WagonEntity wagon) {
        this.wagon = wagon;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }
}
