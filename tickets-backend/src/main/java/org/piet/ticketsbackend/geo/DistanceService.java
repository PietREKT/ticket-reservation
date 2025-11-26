package org.piet.ticketsbackend.geo;

import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.stations.entites.Station;

public interface DistanceService {
    public double distance(Station a, Station b);

    public double routeLength(Route route);
}
