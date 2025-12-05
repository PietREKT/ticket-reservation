package org.piet.ticketsbackend.trains.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TrainCreateDto {

    @NotBlank
    @Size(max = 100)
    private String model;

    @NotBlank
    @Size(max = 100)
    private String number;

    @Size(max = 50)
    private String lineNumber;

    @Min(0)
    private Integer speed;

    @Min(0)
    private Integer wagonCount;

    // na razie tylko w DTO – relacja z trasą może powstać później
    private Long routeId;

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
