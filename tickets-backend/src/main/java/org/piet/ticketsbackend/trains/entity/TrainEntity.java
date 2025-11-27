package org.piet.ticketsbackend.trains.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pociag")
public class TrainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "numer_pociagu", nullable = false)
    private String number;

    @Column(name = "numer_linii")
    private String lineNumber;

    @Column(name = "predkosc")
    private Integer speed;

    @Column(name = "ilosc_wagonow")
    private Integer wagonCount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getWagonCount() {
        return wagonCount;
    }

    public void setWagonCount(Integer wagonCount) {
        this.wagonCount = wagonCount;
    }
}
