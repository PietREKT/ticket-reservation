package org.piet.ticketsbackend.globals.exceptions.handlers;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.piet.ticketsbackend.globals.exceptions.TeapotException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FilterExceptionHandler extends OncePerRequestFilter {
    private final MessageSource messageSource;
    private final ObjectMapper mapper;

    private void mapResponse(HttpServletResponse response, HttpServletRequest request, HttpStatus status, String message) throws IOException {
        var res = ExceptionUtils.prepareResponseEntity(status,
                message,
                request
        );

        response.setStatus(res.getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        var body = res.getBody();
        mapper.writeValue(response.getWriter(), body);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {

            if (response.isCommitted()) {
                throw e;
            }

            mapResponse(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    messageSource.getMessage("error.filter_error",
                            null,
                            LocaleContextHolder.getLocale()
                    )
            );
        } catch (TeapotException ex) {
            if (response.isCommitted()) {
                throw ex;
            }

            mapResponse(
                    response,
                    request,
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage()
            );
        }
    }
}
