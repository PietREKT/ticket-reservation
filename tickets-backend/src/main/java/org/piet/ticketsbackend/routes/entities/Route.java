package org.piet.ticketsbackend.routes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.piet.ticketsbackend.stations.entites.Station;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Entity
@Getter
@Setter
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @OrderBy("position ASC ")
    List<RouteStop> stops;

    @NotBlank
    @Column(unique = true)
    String name;

    Double length;

    Integer totalTimeMinutes;

    boolean active = true;

    public List<Station> getStationsInOrder() {
        return stops
                .stream()
                .map(RouteStop::getStation)
                .toList();
    }
}
