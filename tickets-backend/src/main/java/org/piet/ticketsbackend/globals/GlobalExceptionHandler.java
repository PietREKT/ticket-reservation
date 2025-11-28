package org.piet.ticketsbackend.globals;

import org.piet.ticketsbackend.globals.exceptions.BadRequestException;
import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private Map<String, String> toMessageJson(String message){
        var map = new HashMap<String, String>();
        map.put("message", message);
        return map;
    }

    @ExceptionHandler(DuplicateStationException.class)
    public ResponseEntity<?> handleDuplicateRecord(DuplicateStationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(toMessageJson(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(toMessageJson(ex.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(toMessageJson(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleInternalError(IllegalArgumentException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(toMessageJson(
                messageSource.getMessage("error.internal_error",
                        null,
                        "Something went wrong. Please try again later.",
                        LocaleContextHolder.getLocale()
                )
        ));
    }
}
