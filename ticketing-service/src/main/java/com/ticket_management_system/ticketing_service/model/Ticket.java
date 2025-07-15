package com.ticket_management_system.ticketing_service.model;

import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.enums.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("tickets")
@CompoundIndex(name = "tenant_status_idx", def = "{'tenantId':1,'status':1}")
public class Ticket {
    @Id
    private String id;
    @NotBlank
    private String tenantId;
    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank @Size(max = 16_384)
    private String description;

    @NotNull
    private TicketType ticketType;

    @NotNull
    private TicketPriority priority= TicketPriority.MEDIUM;
    @NotNull
    private TicketStatus status = TicketStatus.NEW;

    private String assigneeId;
    private String nextActionOwnerId;
    @NotNull
    @CreatedDate
    private Instant createdAt;

    @NotNull
    @LastModifiedDate
    private Instant updatedAt;

    private Instant dueAt;
    private Boolean breached=false;
}
