package com.ticket_management_system.ticketing_service.exceptions;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TicketNotFoundException extends RuntimeException {
    private final String ticketId;
    public TicketNotFoundException(String id) {
        super("Cannot find ticket " + id);
        this.ticketId = id;
    }
}
