package org.piet.ticketsbackend.users.services;

import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.globals.jwt.JwtService;
import org.piet.ticketsbackend.users.dtos.UserDto;
import org.piet.ticketsbackend.users.entities.Role;
import org.piet.ticketsbackend.users.entities.User;
import org.piet.ticketsbackend.users.repositories.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public User getFromAuthentication(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof UserDto su)) {
            throw new InsufficientAuthenticationException(
                    messageSource.getMessage("error.users.not_authenticated",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }
        return getById(su.getId());
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("error.users.not_found.username",
                        new Object[]{username},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Override
    public User getById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(() -> new NotFoundException(
                messageSource.getMessage("error.users.not_found.id",
                        new Object[]{uuid},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Override
    public User createUser(String username, String password, Role role, User requestAuthor) throws AccessDeniedException {
        User user = new User();
        user.setUsername(username);
        if (role == Role.ADMIN &&
                (
                        requestAuthor == null ||
                                !requestAuthor.getRole().hasAccessAtLeast(Role.ADMIN)
                )
        ) {
            throw new AccessDeniedException(
                    messageSource.getMessage("error.user.register.no_perms",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }
        user.setRole(role);

        if (role.equals(Role.ADMIN)){
            user.setPasswordNeedsChanging(true);
        }

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }


    @Override
    public String loginUser(String username, String password) {
        try {
            User user = getByUsername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                return jwtService.generateToken(user);
            }
            throw new BadCredentialsException(
                    messageSource.getMessage("error.users.login.password",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        } catch (NotFoundException ex) {
            throw new BadCredentialsException(
                    messageSource.getMessage("error.users.login.username",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }
    }

    @Override
    public void deleteUser(UUID id, User requestAuthor) throws AccessDeniedException {
        User toDelete = getById(id);
        if (!toDelete.equalsUser(requestAuthor) && !requestAuthor.hasAccessAtLeast(Role.ADMIN))
            throw new AccessDeniedException(
                    messageSource.getMessage("error.users.delete.no_perms",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        userRepository.delete(toDelete);
    }

    @Override
    public String changePassword(UUID id, String newPassword, User requestAuthor) throws AccessDeniedException {
        User user = getById(id);
        if (!user.equalsUser(requestAuthor)) {
            throw new AccessDeniedException(
                    messageSource.getMessage("error.users.password.no_perms",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPasswordNeedsChanging(false);
        user = userRepository.save(user);
        return jwtService.generateToken(user);
    }
}
