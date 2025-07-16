package com.ticket_management_system.ticketing_service.dto.request;

import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import jakarta.validation.constraints.Size;

public record UpdateTicketRequest(
      String title,
      @Size(max = 16_384) String description,
      TicketPriority priority,
      TicketStatus ticketStatus,
      String assigneeId,
      String nextActionOwnerId) {
}
