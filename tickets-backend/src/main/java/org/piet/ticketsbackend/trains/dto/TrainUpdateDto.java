package org.piet.ticketsbackend.trains.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TrainUpdateDto {

    @NotBlank
    @Size(max = 100)
    private String model;

    @NotBlank
    @Size(max = 100)
    private String name;

    private Long routeId;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }
}
