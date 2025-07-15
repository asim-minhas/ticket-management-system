package com.ticket_management_system.ticketing_service.model;

import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("ticket_histories")
@CompoundIndexes({
        @CompoundIndex(name = "hist_ticket_idx", def = "{'ticketId':1,'changedAt':1}")
})
public class TicketHistory {
    @Id
    private String id;

    @NotNull
    private String ticketId;

    @NotNull
    private TicketStatus fromStatus;
    @NotNull
    private TicketStatus toStatus;
    @NotBlank
    private String changedBy;
    @NotNull
    @CreatedDate
    private Instant changedAt;
    @NotBlank
    @Size(max = 16_384)
    private String comment;
}
