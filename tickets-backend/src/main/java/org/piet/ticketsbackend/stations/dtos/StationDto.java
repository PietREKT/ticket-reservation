package org.piet.ticketsbackend.stations.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.piet.ticketsbackend.stations.entities.Station;

import java.io.Serializable;

/**
 * DTO for {@link org.piet.ticketsbackend.stations.entities.Station}
 */
@Value
public class StationDto implements Serializable {
    Long id;
    @Length(min = 3, max = 5)
    String code;
    @Length(min = 2, max = 4)
    String countryCode;
    @NotBlank
    String city;

    String fullName;

    public static StationDto create(Station station){
        return new StationDto(
                station.getId(),
                station.getCode(),
                station.getCountryCode(),
                station.getCity(),
                station.getFullName()
        );
    }
}