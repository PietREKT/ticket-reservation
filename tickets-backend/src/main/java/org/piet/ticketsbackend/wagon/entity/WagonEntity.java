package org.piet.ticketsbackend.wagon.entity;

import jakarta.persistence.*;
import org.piet.ticketsbackend.trains.entity.TrainEntity;

@Entity
@Table(name = "wagon")
public class WagonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numer", nullable = false)
    private Integer number;

    @Column(name = "miejsca_ogolnie", nullable = false)
    private Integer totalSeats;

    @Column(name = "miejsca_zajete", nullable = false)
    private Integer occupiedSeats = 0;

    @Column(name = "klasa", nullable = false)
    private String wagonClass;

    @ManyToOne
    @JoinColumn(name = "id_pociagu", nullable = false)
    private TrainEntity train;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getOccupiedSeats() {
        return occupiedSeats;
    }

    public void setOccupiedSeats(Integer occupiedSeats) {
        this.occupiedSeats = occupiedSeats;
    }

    public String getWagonClass() {
        return wagonClass;
    }

    public void setWagonClass(String wagonClass) {
        this.wagonClass = wagonClass;
    }

    public TrainEntity getTrain() {
        return train;
    }

    public void setTrain(TrainEntity train) {
        this.train = train;
    }
}
