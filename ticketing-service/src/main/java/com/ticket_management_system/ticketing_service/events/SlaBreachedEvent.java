package com.ticket_management_system.ticketing_service.events;

import java.time.Instant;

public class SlaBreachedEvent {
    private final String ticketId;
    private final Instant breachedAt;

    public SlaBreachedEvent(String ticketId, Instant breachedAt) {
        this.ticketId  = ticketId;
        this.breachedAt = breachedAt;
    }

    public String getTicketId()  { return ticketId; }
    public Instant getBreachedAt() { return breachedAt; }
}
