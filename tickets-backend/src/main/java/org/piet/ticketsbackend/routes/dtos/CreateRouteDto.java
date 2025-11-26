package org.piet.ticketsbackend.routes.dtos;

import jakarta.annotation.Nullable;
import lombok.Value;
import org.hibernate.validator.constraints.Length;
import org.piet.ticketsbackend.routes.entities.Route;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Route}
 */
@Value
public class CreateRouteDto implements Serializable {
    @Length(min = 2)
    List<String> stops;
    String routeName;
    Boolean circular;
}