package org.piet.ticketsbackend.connectionfinder.models;

import lombok.Value;
import org.piet.ticketsbackend.connectionfinder.dtos.ConnectionLegDto;
import org.piet.ticketsbackend.stations.entities.Station;

import java.time.LocalDateTime;
import java.util.List;

@Value
public class SearchState {
    Station station;
    LocalDateTime arrivalTime;
    List<ConnectionLegDto> legs; //how we got there
}
