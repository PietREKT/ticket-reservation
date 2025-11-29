package org.piet.ticketsbackend.globals.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.piet.ticketsbackend.globals.exceptions.TeapotException;
import org.piet.ticketsbackend.globals.jwt.JwtService;
import org.piet.ticketsbackend.users.dtos.UserDto;
import org.piet.ticketsbackend.users.entities.User;
import org.piet.ticketsbackend.users.repositories.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Log4j2
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final MessageSource messageSource;

    public JwtFilter(JwtService jwtService, UserRepository userRepository, MessageSource messageSource) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token.equalsIgnoreCase("teapot") ||
                token.contains("teapot") ||
                token.contains("coffee") ||
                token.equalsIgnoreCase("coffee")
        ){
            throw new TeapotException("What did you expect?");
        }

        Claims claims = jwtService.parse(token).getPayload();
        String idString = claims.get("id", String.class);

        if (idString == null || idString.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID id = UUID.fromString(idString);
            userRepository.findById(id).ifPresentOrElse(u -> {
                UserDto userDto = UserDto.create(u);
                var auth = new UsernamePasswordAuthenticationToken(userDto, null, userDto.getRole().getAsAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }, () -> log.info("User with id: {} in token not found!", id));

            filterChain.doFilter(request, response);
        } catch (IllegalArgumentException ex) {
            throw new JwtException(
                    messageSource.getMessage("error.filter_error",
                            null,
                            LocaleContextHolder.getLocale()
                    ),
                    ex
            );
        }
    }
}
