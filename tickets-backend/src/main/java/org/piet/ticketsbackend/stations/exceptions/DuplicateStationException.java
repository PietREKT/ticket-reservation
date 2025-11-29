package org.piet.ticketsbackend.stations.exceptions;

public class DuplicateStationException extends RuntimeException{
    public DuplicateStationException(String message) {
        super(message);
    }
}
