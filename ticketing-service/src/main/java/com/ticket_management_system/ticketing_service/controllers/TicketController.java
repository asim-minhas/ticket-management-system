package com.ticket_management_system.ticketing_service.controllers;

import com.ticket_management_system.ticketing_service.dto.request.AssignTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.CreateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.StatusTransitionRequest;
import com.ticket_management_system.ticketing_service.dto.request.UpdateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.response.TicketResponse;
import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.exceptions.IllegalTransitionException;
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
import io.swagger.v3.oas.annotations.Operation;


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

    @Operation(summary = "Create a new ticket")
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

    @Operation(summary = "update ticket")
    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(@PathVariable String id,@Valid @RequestBody UpdateTicketRequest request) throws TicketNotFoundException {
        Ticket updated = ticketService.updateTicket(id,request);
        return ResponseEntity.ok(mapToResponse(updated));
    }

    @Operation(summary = "Get a ticket by Ticket Id")
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable String id) throws TicketNotFoundException {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mapToResponse(ticket));
    }

    @Operation(summary = "Retrieve all tickets")
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


    /** TICK-5: status transition */
    @Operation(summary = "Change status of a ticket")
    @PostMapping("/{id}/status")
    public ResponseEntity<TicketResponse> transitionStatus(
            @PathVariable String id,
            @Valid @RequestBody StatusTransitionRequest req
    ) throws TicketNotFoundException, IllegalTransitionException {
        Ticket t = ticketService.transitionStatusTicket(id, req);
        return ResponseEntity.ok(mapToResponse(t));
    }

    /** TICK-4 (optional dedicated assign) */
    @Operation(summary = "Assign ticket to someone")
    @PostMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assignTicket(
            @PathVariable String id,
            @Valid @RequestBody AssignTicketRequest req
    ) throws TicketNotFoundException {
        Ticket t = ticketService.assignTicket(id, req);
        return ResponseEntity.ok(mapToResponse(t));
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        long slaRemainingDays = Duration.between(Instant.now(), ticket.getDueAt())
                .toDays();
        return new TicketResponse(
                ticket.getTicketId(),
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
                slaRemainingDays
        );
    }
}
