package com.ticket_management_system.ticketing_service.dto.request;

import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record StatusTransitionRequest(
    @NotNull TicketStatus newStatus,
    @NotBlank @Size(max = 16_384) String comment
) {}
