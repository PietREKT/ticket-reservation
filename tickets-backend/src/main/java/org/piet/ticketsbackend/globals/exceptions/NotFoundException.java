package org.piet.ticketsbackend.globals.exceptions;

import jakarta.servlet.http.HttpServletRequest;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
