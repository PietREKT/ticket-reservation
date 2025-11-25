package org.piet.ticketsbackend.stations.services;

import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StationService {
    public Station getStationById(Long id);

    public Station getStationByCode(String code);

    public Station createStation(String code, String countryCode, String city, double x, double y, String description) throws DuplicateStationException;

    public Station createStation(String code, String countryCode, String city, double x, double y);

    public Page<Station> getStationsByCity(String city, Pageable pageable);
}
