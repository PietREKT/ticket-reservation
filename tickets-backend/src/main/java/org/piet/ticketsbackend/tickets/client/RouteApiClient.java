package org.piet.ticketsbackend.tickets.client;

import org.piet.ticketsbackend.geo.DistanceService;
import org.piet.ticketsbackend.routes.entities.Route;
import org.piet.ticketsbackend.routes.services.RouteService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// SOLID: SRP - wyłącznie pobieranie informacji o trasie (bez zapisywania/alokacji)
// SOLID: DIP - konstruktor wstrzykuje zależności (RouteService, DistanceService)
@Component
public class RouteApiClient {

    private final RouteService routeService;
    private final DistanceService distanceService;

    // SOLID: DIP - przez konstruktor
    public RouteApiClient(RouteService routeService, DistanceService distanceService) {
        this.routeService = routeService;
        this.distanceService = distanceService;
    }

    // SOLID: SRP - obliczanie informacji o trasie
    public RouteInfo getRouteInfo(Long routeId, LocalDate travelDate,
                                  String startStationCode, String endStationCode) {
        Route route = routeService.getRouteById(routeId);
        double distanceKm = distanceService.routeLength(route);

        // SOLID: SRP - wyodrębniona logika obliczania ceny
        BigDecimal basePrice = BigDecimal.valueOf(distanceKm)
                .multiply(new BigDecimal("0.5"))
                .setScale(2, RoundingMode.HALF_UP);

        LocalDateTime departure = LocalDateTime.of(travelDate, LocalTime.of(8, 0));

        return new RouteInfo(route.getId(), startStationCode, endStationCode,
                departure, basePrice);
    }

    // SOLID: OCP - Open-Closed dla rozszerzeń
    public static class RouteInfo {
        private final Long routeId;
        private final String startStationName;
        private final String endStationName;
        private final LocalDateTime departureTime;
        private final BigDecimal basePrice;

        public RouteInfo(Long routeId, String startStationName, String endStationName,
                         LocalDateTime departureTime, BigDecimal basePrice) {
            this.routeId = routeId;
            this.startStationName = startStationName;
            this.endStationName = endStationName;
            this.departureTime = departureTime;
            this.basePrice = basePrice;
        }

        // Gettery
        public Long getRouteId() { return routeId; }
        public String getStartStationName() { return startStationName; }
        public String getEndStationName() { return endStationName; }
        public LocalDateTime getDepartureTime() { return departureTime; }
        public BigDecimal getBasePrice() { return basePrice; }
    }
}
