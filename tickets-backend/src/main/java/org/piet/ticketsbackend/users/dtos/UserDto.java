package org.piet.ticketsbackend.users.dtos;

import lombok.Value;
import org.piet.ticketsbackend.users.entities.Role;
import org.piet.ticketsbackend.users.entities.User;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link org.piet.ticketsbackend.users.entities.User}
 */
@Value
public class UserDto implements Serializable {
    UUID id;
    String username;
    Role role;
    boolean passwordNeedsChanging;


    public static UserDto create(User user){
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isPasswordNeedsChanging()
        );
    }
}