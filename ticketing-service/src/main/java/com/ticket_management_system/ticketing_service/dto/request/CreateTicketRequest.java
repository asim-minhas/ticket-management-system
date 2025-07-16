package com.ticket_management_system.ticketing_service.dto.request;

import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.enums.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTicketRequest(
    @NotBlank @Size(max = 100) String title,
    @NotBlank @Size(max = 16_384) String description,
    @NotNull TicketType ticketType,
    TicketPriority priority,
    String assigneeId,
    String nextActionOwnerId,
    @NotBlank  String tenantId) {
}
