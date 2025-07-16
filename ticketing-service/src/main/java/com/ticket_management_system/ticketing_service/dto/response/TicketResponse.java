package com.ticket_management_system.ticketing_service.dto.response;

import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.enums.TicketType;
import com.ticket_management_system.ticketing_service.model.Ticket;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record TicketResponse(  String id,
                               String title,
                               String description,
                               TicketType ticketType,
                               TicketPriority priority,
                               TicketStatus status,
                               String assigneeId,
                               String nextActionOwnerId,
                               Instant createdAt,
                               Instant updatedAt,
                               Instant dueAt,
                               boolean breached,
                               long slaRemainingMinutes) {
}
