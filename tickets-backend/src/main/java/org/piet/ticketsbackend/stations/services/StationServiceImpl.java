package org.piet.ticketsbackend.stations.services;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.piet.ticketsbackend.globals.dtos.PageDto;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.stations.entites.Station;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.piet.ticketsbackend.stations.repositories.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;
    private final MessageSource messageSource;

    @Value("${app.geometry.srid}")
    private Integer srid;

    public StationServiceImpl(StationRepository stationRepository, MessageSource messageSource) {
        this.stationRepository = stationRepository;
        this.messageSource = messageSource;
    }

    @Override
    public Station getStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("error.stations.not_found.id",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Override
    public Station getStationByCode(String code) {
        return stationRepository.findByCode(code).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("error.stations.not_found.code",
                        new Object[]{code},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    private Station initStation(String code, String countryCode, String city, double x, double y) {
        Point location = new GeometryFactory(new PrecisionModel(), srid)
                .createPoint(new Coordinate(x, y));

        Station station = new Station();
        station.setCity(city);
        station.setCountryCode(countryCode);
        station.setCode(code);
        station.setLocation(location);

        return station;
    }

    @Override
    public Station createStation(String code, String countryCode, String city, double x, double y, String description) throws DuplicateStationException {
        Station s = initStation(code, countryCode, city, x, y);
        s.setDescription(description);
        try {
            return stationRepository.save(s);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateStationException(
                    messageSource.getMessage("error.stations.duplicate",
                            new Object[]{code},
                            LocaleContextHolder.getLocale()
                    )
            );
        }
    }

    @Override
    public Station createStation(String code, String countryCode, String city, double x, double y) {
        try{
            return stationRepository.save(initStation(code, countryCode, city, x, y));
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateStationException(
                    messageSource.getMessage("error.stations.duplicate",
                            new Object[]{code},
                            LocaleContextHolder.getLocale()
                    )
            );
        }
    }

    @Override
    public Page<Station> getStationsByCity(String city, Pageable pageable) {
        return stationRepository.findByCity(city, pageable);
    }
}
