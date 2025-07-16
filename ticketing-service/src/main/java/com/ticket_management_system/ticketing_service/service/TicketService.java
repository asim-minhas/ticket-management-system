package com.ticket_management_system.ticketing_service.service;

import com.ticket_management_system.ticketing_service.dto.request.CreateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.UpdateTicketRequest;
import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.exceptions.TicketNotFoundException;
import com.ticket_management_system.ticketing_service.model.Ticket;

import java.util.List;

public interface TicketService {
    Ticket createTicket(CreateTicketRequest request);
    Ticket getTicketById(String ticketId) throws TicketNotFoundException;
    Ticket updateTicket(String id,UpdateTicketRequest request) throws TicketNotFoundException;
    List<Ticket> listTickets(TicketStatus status, TicketPriority priority, String assigneeId, String tenantId);
}
