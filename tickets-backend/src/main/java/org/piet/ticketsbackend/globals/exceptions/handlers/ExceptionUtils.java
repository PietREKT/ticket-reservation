package org.piet.ticketsbackend.globals.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionUtils {
    public static ResponseEntity<?> prepareResponseEntity(HttpStatus status, Exception exception, HttpServletRequest request){
        return prepareResponseEntity(status, exception.getMessage(), request);
    }

    public static ResponseEntity<?> prepareResponseEntity(HttpStatus status, String exceptionMessage, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", exceptionMessage);
        body.put("status", status.value());
        body.put("path", request.getRequestURI());
        body.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

        return ResponseEntity.status(status).body(body);
    }

    public static <T> ResponseEntity<?> prepareResponseEntity(HttpStatus status, Map<String, T> errors, HttpServletRequest request) {
        return prepareResponseEntity(status, List.of(errors), request);
    }

    public static <T> ResponseEntity<?> prepareResponseEntity(HttpStatus status, List<Map<String, T>> errors, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("errors", errors);
        body.put("status", status.value());
        body.put("path", request.getRequestURI());
        body.put("timestamp", DateTimeFormatter.ISO_INSTANT.format(Instant.now()));

        return ResponseEntity.status(status).body(body);
    }


}
