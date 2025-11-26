package org.piet.ticketsbackend.routes.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.piet.ticketsbackend.stations.entites.Station;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "position"}))
@NoArgsConstructor
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    private int position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "station_id")
    private Station station;

    @ManyToOne(optional = false)
    @JoinColumn(name = "route_id")
    private Route route;

    public RouteStop(int position, Station station, Route route) {
        this.position = position;
        this.station = station;
        this.route = route;
    }
}
