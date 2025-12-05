package org.piet.ticketsbackend.wagons.entity;

import jakarta.persistence.*;
import org.piet.ticketsbackend.trains.entity.TrainEntity;

@Entity
@Table(name = "wagon")
public class WagonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numer", nullable = false)
    private String number;

    @Column(name = "miejsca_ogolnie", nullable = false)
    private Integer seatsTotal;

    @Column(name = "miejsca_wolne", nullable = false)
    private Integer seatsFree;

    @Column(name = "klasa", nullable = false)
    private String seatClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pociag_id", nullable = false)
    private TrainEntity train;

    public WagonEntity() {
    }

    public WagonEntity(
            Long id,
            String number,
            Integer seatsTotal,
            Integer seatsFree,
            String seatClass,
            TrainEntity train
    ) {
        this.id = id;
        this.number = number;
        this.seatsTotal = seatsTotal;
        this.seatsFree = seatsFree;
        this.seatClass = seatClass;
        this.train = train;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getSeatsTotal() {
        return seatsTotal;
    }

    public void setSeatsTotal(Integer seatsTotal) {
        this.seatsTotal = seatsTotal;
    }

    public Integer getSeatsFree() {
        return seatsFree;
    }

    public void setSeatsFree(Integer seatsFree) {
        this.seatsFree = seatsFree;
    }

    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }

    public TrainEntity getTrain() {
        return train;
    }

    public void setTrain(TrainEntity train) {
        this.train = train;
    }
}
