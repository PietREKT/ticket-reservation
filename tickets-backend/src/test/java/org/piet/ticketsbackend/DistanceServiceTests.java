package org.piet.ticketsbackend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.mockito.InjectMocks;
import org.piet.ticketsbackend.geo.DistanceServiceImpl;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.entities.RouteStop;
import org.piet.ticketsbackend.stations.entities.Station;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceServiceTests {
    @InjectMocks
    DistanceServiceImpl service = new DistanceServiceImpl();

    private final GeometryFactory geoFactory = new GeometryFactory(new PrecisionModel(), 4326);

    private Point createPoint(double x, double y){
        return geoFactory.createPoint(new Coordinate(x, y));
    }

    @BeforeEach
    void setUp(){
        ReflectionTestUtils.setField(service, "avgTrainSpeed", 80.0);
    }

    @Test
    void distanceShouldBeZeroForTheSameLocation(){
        Station a = new Station();
        Station b = new Station();
        a.setLocation(createPoint(10.0, 20.0));
        b.setLocation(createPoint(10.0, 20.0));

        assertEquals(0.0, service.distance(a, b));
    }

    @Test
    void distanceShouldBeProperlyCalculated(){
        Station a = new Station();
        Station b = new Station();
        a.setLocation(createPoint(20.9808661, 52.2240456));
        b.setLocation(createPoint(21.98504, 51.42648));

        assertEquals(112.4, service.distance(a, b), 1.0);
    }

    @Test
    void routeLengthShouldBeProperlyCalculated(){
        Route route = new Route();

        Station a = new Station();
        Station b = new Station();
        Station c = new Station();

        a.setLocation(createPoint(20.9808661, 52.2240456));
        b.setLocation(createPoint(21.98504, 51.42648));
        c.setLocation(createPoint(22.5689492, 51.2311725));

        RouteStop stopA = new RouteStop(0, a, route);
        RouteStop stopB = new RouteStop(1, b, route);
        RouteStop stopC = new RouteStop(2, c, route);

        List<RouteStop> stops = List.of(stopA, stopB, stopC);
        route.setStops(stops);

        assertEquals(158.0, service.routeLength(route), 1.0);
    }
}
