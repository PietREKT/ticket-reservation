package org.piet.ticketsbackend.geo;

import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.stations.entities.Station;

public interface DistanceService {
    public double distance(Station a, Station b);

    public double routeLength(Route route);

    public int travelTimeInMinutes(Route route, Double trainSpeed);
    default public int travelTimeInMinutes(Route route){
        return travelTimeInMinutes(route, null);
    };

    public int timeBetweenStations(Station a, Station b, Double trainSpeed);

    default public int timeBetweenStations(Station a, Station b){
        return timeBetweenStations(a, b, null);
    };
}
