package com.ticket_management_system.ticketing_service.exceptions;

public class IllegalTransitionException extends RuntimeException {
    public IllegalTransitionException(String from, String to) {
        super("Cannot transition ticket from " + from + " to " + to);
    }
}
