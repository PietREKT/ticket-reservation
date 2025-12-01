package org.piet.ticketsbackend.trains.dto;

public class TrainDto {

    private Long id;
    private String model;
    private String name;
    private Long routeId;

    public TrainDto() {
    }

    public TrainDto(Long id, String model, String name, Long routeId) {
        this.id = id;
        this.model = model;
        this.name = name;
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
