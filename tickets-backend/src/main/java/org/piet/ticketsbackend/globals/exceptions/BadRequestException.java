package org.piet.ticketsbackend.globals.exceptions;

import jakarta.servlet.http.HttpServletRequest;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
