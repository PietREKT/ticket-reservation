package org.piet.ticketsbackend.globals.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.globals.exceptions.TeapotException;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

import static org.piet.ticketsbackend.globals.exceptions.handlers.ExceptionUtils.prepareResponseEntity;

@RestControllerAdvice
@RequiredArgsConstructor
@Log4j2
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(DuplicateStationException.class)
    public ResponseEntity<?> handleDuplicateRecord(DuplicateStationException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.NOT_FOUND, ex, request);
    }

    // Login / auth exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleNotAuthorized(AuthenticationException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleNotAuthorized(BadCredentialsException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleForbidden(AccessDeniedException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.FORBIDDEN, ex, request);
    }

    // Request exceptions
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        var errors = ex
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> Map.of(
                        "field", fe.getField(),
                        "message", fe.getDefaultMessage()
                ))
                .toList();
        return prepareResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingPathVariableException.class,
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestPartException.class,
            MissingRequestHeaderException.class
    })
    public ResponseEntity<?> handleBadRequestMvc(Exception ex, HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
                                                    HttpServletRequest request) {
        return prepareResponseEntity(HttpStatus.METHOD_NOT_ALLOWED, ex, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request){
        var errors = ex.getConstraintViolations()
                .stream()
                .map(cv -> Map.of(
                        "field", cv.getPropertyPath().toString(),
                        "message", cv.getMessage()
                ))
                .toList();
        return prepareResponseEntity(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleUnreadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return prepareResponseEntity(
                HttpStatus.BAD_REQUEST,
                messageSource.getMessage("error.requests.unreadable",
                        null,
                        "Request body is malformed or missing",
                        LocaleContextHolder.getLocale()
                ),
                request
        );
    }


    // Generic handler for all remaining exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleInternalError(RuntimeException ex, HttpServletRequest request) {

        if (ex instanceof ResponseStatusException rex) {
            throw rex;
        }

        return prepareResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR,
                messageSource.getMessage("error.internal_error",
                        null,
                        "Something went wrong. Please try again later.",
                        LocaleContextHolder.getLocale()
                ),
                request
        );
    }
}
