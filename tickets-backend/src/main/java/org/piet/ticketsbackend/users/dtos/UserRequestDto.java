package org.piet.ticketsbackend.users.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link org.piet.ticketsbackend.users.entities.User}
 */
@Value
public class UserRequestDto implements Serializable {
    @NotBlank String username;
    @NotBlank String password;
}