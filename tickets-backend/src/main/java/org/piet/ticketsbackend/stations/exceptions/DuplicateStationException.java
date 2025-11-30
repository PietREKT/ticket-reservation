package org.piet.ticketsbackend.stations.exceptions;

import jakarta.servlet.http.HttpServletRequest;

public class DuplicateStationException extends RuntimeException {
    public DuplicateStationException(String message) {
        super(message);
    }
}
