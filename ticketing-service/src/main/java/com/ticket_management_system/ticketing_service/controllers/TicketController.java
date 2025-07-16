package com.ticket_management_system.ticketing_service.controllers;

import com.ticket_management_system.ticketing_service.dto.request.CreateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.UpdateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.response.TicketResponse;
import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.exceptions.TicketNotFoundException;
import com.ticket_management_system.ticketing_service.model.Ticket;
import com.ticket_management_system.ticketing_service.service.TicketService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request){
        Ticket ticket = ticketService.createTicket(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ticket.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(mapToResponse(ticket));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable String id,@Valid @RequestBody UpdateTicketRequest request) throws TicketNotFoundException {
        Ticket updated = ticketService.updateTicket(id,request);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable String id) throws TicketNotFoundException {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapToResponse(ticket));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listTickets(
            @RequestParam @NotBlank String tenantId,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority,
            @RequestParam(required = false) String assigneeId) {

        List<Ticket> tickets = ticketService.listTickets(status, priority, assigneeId,tenantId);
        List<TicketResponse> responses = tickets.stream()
                .map(this::mapToResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        long slaRemaining = Duration.between(Instant.now(), ticket.getDueAt())
                .toMinutes();
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getTicketType(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAssigneeId(),
                ticket.getNextActionOwnerId(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getDueAt(),
                ticket.getBreached(),
                slaRemaining
        );
    }
}
