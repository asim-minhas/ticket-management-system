package com.ticket_management_system.ticketing_service.service.serviceimpl;

import com.ticket_management_system.ticketing_service.dto.request.CreateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.UpdateTicketRequest;
import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.exceptions.TicketNotFoundException;
import com.ticket_management_system.ticketing_service.model.Ticket;
import com.ticket_management_system.ticketing_service.repositories.TicketRepository;
import com.ticket_management_system.ticketing_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final MongoTemplate mongo;

    public TicketServiceImpl(TicketRepository ticketRepository, MongoTemplate mongo) {
        this.ticketRepository = ticketRepository;
        this.mongo = mongo;
    }

    @Override
    public Ticket createTicket(CreateTicketRequest request) {

        String tenantId = request.tenantId();
        TicketPriority priority = Optional.ofNullable(request.priority()).orElse(TicketPriority.MEDIUM);
        String assigneeId = request.assigneeId();
        String nextActionOwnerId = request.nextActionOwnerId();

        Instant slaTimer = Instant.now().plus(15000,MINUTES);

        Ticket ticket = Ticket.builder()
                .ticketType(request.ticketType())
                .assigneeId(assigneeId)
                .description(request.description())
                .priority(priority)
                .title(request.title())
                .status(TicketStatus.NEW)
                .tenantId(tenantId)
                .nextActionOwnerId(nextActionOwnerId)
                .dueAt(slaTimer)
                .breached(false)
                .build();
        ticketRepository.save(ticket);
        return ticket;
    }

    @Override
    public Ticket getTicketById(String ticketId) throws TicketNotFoundException {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    @Override
    public Ticket updateTicket(String id,UpdateTicketRequest request) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

              // apply only the fields the client provided
        if (request.title() != null)       ticket.setTitle(request.title());
        if (request.description() != null) ticket.setDescription(request.description());
        if (request.priority() != null)    ticket.setPriority(request.priority());
        if (request.assigneeId() != null)  ticket.setAssigneeId(request.assigneeId());
        if (request.ticketStatus() != null)  ticket.setStatus(request.ticketStatus());
        if (request.nextActionOwnerId() != null) {
            ticket.setNextActionOwnerId(request.nextActionOwnerId());
        }

        ticketRepository.save(ticket);

        return ticket;
    }

    @Override
    public List<Ticket> listTickets(TicketStatus status, TicketPriority priority, String assigneeId, String tenantId) {

        Query q = new Query(Criteria.where("tenantId").is(tenantId));
        if (status != null)        q.addCriteria(Criteria.where("status").is(status));
        if (priority != null)      q.addCriteria(Criteria.where("priority").is(priority));
        if (assigneeId != null)    q.addCriteria(Criteria.where("assigneeId").is(assigneeId));

        return mongo.find(q, Ticket.class);
    }
}
