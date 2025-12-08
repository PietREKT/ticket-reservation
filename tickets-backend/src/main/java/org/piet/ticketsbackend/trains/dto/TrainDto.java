package org.piet.ticketsbackend.trains.dto;

public class TrainDto {

    private Long id;
    private String model;
    private String number;
    private String lineNumber;
    private Integer speed;
    private Integer wagonCount;
    private Long routeId;

    public TrainDto() {
    }

    public TrainDto(
            Long id,
            String model,
            String number,
            String lineNumber,
            Integer speed,
            Integer wagonCount,
            Long routeId
    ) {
        this.id = id;
        this.model = model;
        this.number = number;
        this.lineNumber = lineNumber;
        this.speed = speed;
        this.wagonCount = wagonCount;
        this.routeId = routeId;
    }

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

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
