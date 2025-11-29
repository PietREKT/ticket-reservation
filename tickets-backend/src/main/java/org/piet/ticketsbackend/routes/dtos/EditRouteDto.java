package org.piet.ticketsbackend.routes.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.piet.ticketsbackend.routes.entities.Route}
 */
@Value
public class EditRouteDto implements Serializable {
    //query data
    Long id;
    String name;

    //edit data
    String newName;
    boolean active;
}