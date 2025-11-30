package org.piet.ticketsbackend.users.services;

import org.piet.ticketsbackend.users.entities.Role;
import org.piet.ticketsbackend.users.entities.User;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface UserService {
    public User getFromAuthentication(Authentication auth);

    public User getByUsername(String username);

    public User getById(UUID uuid);

    public User createUser(String username, String password, Role role, User requestAuthor) throws AccessDeniedException;
    default public User createUser(String username, String password, Role role) throws AccessDeniedException {
        return createUser(username, password, role, null);
    };

    public void deleteUser(UUID id, User requestAuthor) throws AccessDeniedException;

    public String changePassword(UUID id, String newPassword, User requestAuthor) throws AccessDeniedException;

    String loginUser(String username, String password);
}
