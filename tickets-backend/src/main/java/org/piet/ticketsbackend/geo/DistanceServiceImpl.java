package org.piet.ticketsbackend.geo;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.locationtech.jts.geom.Point;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DistanceServiceImpl implements DistanceService{

    private final GeodeticCalculator calculator = new GeodeticCalculator();

    @Value("${app.trains.avgSpeed}")
    Double avgTrainSpeed;

    @Override
    public double distance(Station a, Station b) {
        Point p1 = a.getLocation();
        Point p2 = b.getLocation();

        GlobalCoordinates coord1 = new GlobalCoordinates(p1.getY(), p1.getX());
        GlobalCoordinates coord2 = new GlobalCoordinates(p2.getY(), p2.getX());

        double dist = calculator.calculateGeodeticCurve(Ellipsoid.WGS84, coord1, coord2).getEllipsoidalDistance();
        return Math.abs(dist) / 1000.0;
    }

    @Override
    public double routeLength(Route route) {
        List<Station> stations = route
                .getStops()
                .stream()
                .map(RouteStop::getStation)
                .toList();

        if (stations.size() < 2) return 0.0;

        return IntStream.range(0, stations.size() - 1)
                .mapToDouble(i -> distance(stations.get(i), stations.get(i + 1)))
                .sum();
    }

    @Override
    public int travelTimeInMinutes(Route route, double trainSpeed) {
        return (int) Math.ceil((routeLength(route) / trainSpeed) * 60);
    }

    @Override
    public int travelTimeInMinutes(Route route){
        return travelTimeInMinutes(route, avgTrainSpeed);
    }

    @Override
    public int timeBetweenStations(Station a, Station b, double trainSpeed) {
        return (int) Math.ceil((distance(a, b) / trainSpeed) * 60);
    }

    @Override
    public int timeBetweenStations(Station a, Station b) {
        return timeBetweenStations(a, b, avgTrainSpeed);
    }
}
