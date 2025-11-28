package org.piet.ticketsbackend.stations.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * DTO for {@link org.piet.ticketsbackend.stations.entities.Station}
 */
@Value
public class CreateStationDto implements Serializable {
    String description;
    @Length(min = 3, max = 5)
    String code;
    @Length(min = 2, max = 4)
    String countryCode;
    @NotBlank
    String city;
    @NotNull
    double x, y;
}