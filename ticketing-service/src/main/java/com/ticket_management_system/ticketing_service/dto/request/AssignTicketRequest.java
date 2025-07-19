package com.ticket_management_system.ticketing_service.dto.request;

import jakarta.validation.constraints.Size;

public record AssignTicketRequest(
    @Size(max = 100) String assigneeId,
    @Size(max = 100) String nextActionOwnerId
) {}
