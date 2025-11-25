package org.piet.ticketsbackend.globals;

import org.piet.ticketsbackend.globals.exceptions.NotFoundException;
import org.piet.ticketsbackend.stations.exceptions.DuplicateStationException;
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
}
