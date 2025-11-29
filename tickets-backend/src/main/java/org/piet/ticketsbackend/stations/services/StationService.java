package org.piet.ticketsbackend.stations.services;

import org.piet.ticketsbackend.globals.dtos.PaginationDto;
import org.piet.ticketsbackend.stations.entities.Station;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StationService {
    public Station getStationById(Long id);

    public Station getStationByCode(String code);

    public Station createStation(String code, String countryCode, String city, double x, double y, String description) throws DuplicateStationException;

    default public Station createStation(String code, String countryCode, String city, double x, double y){
        return createStation(code, countryCode, city, x, y, null);
    };

    public Page<Station> getStationsByCity(String city, Pageable pageable);

    public Page<Station> getAll(Pageable pageable);
    default public Page<Station> getAll(PaginationDto dto){
        return getAll(dto.toPageable());
    };

    public void deleteStation(Long id);
}
