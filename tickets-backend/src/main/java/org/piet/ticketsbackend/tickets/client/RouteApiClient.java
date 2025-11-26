package org.piet.ticketsbackend.tickets.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class RouteApiClient {

    public RouteInfo getRouteInfo(Long routeId, LocalDate date, String start, String end) {
        return new RouteInfo(routeId, start, end,
                LocalDateTime.now().plusHours(2), new BigDecimal("100"));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RouteInfo {
        private Long routeId;
        private String startStationName;
        private String endStationName;
        private LocalDateTime departureTime;
        private BigDecimal basePrice;
    }
}
