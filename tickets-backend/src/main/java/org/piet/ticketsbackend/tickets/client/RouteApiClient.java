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

    public RouteInfo getRouteInfo(Long routeId,
                                  LocalDate date,
                                  String startStationCode,
                                  String endStationCode) {

        return new RouteInfo(
                routeId,
                "MockStart",
                "MockEnd",
                LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 12, 0),
                new BigDecimal("100.00")
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteInfo {
        private Long routeId;
        private String startStationName;
        private String endStationName;
        private LocalDateTime departureTime;
        private BigDecimal basePrice;
    }
}
