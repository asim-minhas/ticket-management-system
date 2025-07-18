package com.ticket_management_system.ticketing_service.service.serviceimpl;

import com.ticket_management_system.ticketing_service.dto.request.AssignTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.CreateTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.StatusTransitionRequest;
import com.ticket_management_system.ticketing_service.dto.request.UpdateTicketRequest;
import com.ticket_management_system.ticketing_service.enums.TicketPriority;
import com.ticket_management_system.ticketing_service.enums.TicketStatus;
import com.ticket_management_system.ticketing_service.exceptions.IllegalTransitionException;
import com.ticket_management_system.ticketing_service.exceptions.TicketNotFoundException;
import com.ticket_management_system.ticketing_service.model.Ticket;
import com.ticket_management_system.ticketing_service.repositories.TicketHistoryRepository;
import com.ticket_management_system.ticketing_service.repositories.TicketRepository;
import com.ticket_management_system.ticketing_service.sequence.TicketKeyGenerator;
import com.ticket_management_system.ticketing_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final MongoTemplate mongo;
    private final TicketHistoryRepository historyRepo;
    private final TicketKeyGenerator keyGenerator;
    private final TicketKeyGenerator ticketKeyGenerator;


    public TicketServiceImpl(TicketRepository ticketRepository, MongoTemplate mongo, TicketHistoryRepository historyRepo, TicketKeyGenerator keyGenerator, TicketKeyGenerator ticketKeyGenerator) {
        this.ticketRepository = ticketRepository;
        this.mongo = mongo;
        this.historyRepo = historyRepo;
        this.keyGenerator = keyGenerator;
        this.ticketKeyGenerator = ticketKeyGenerator;
    }

    @Override
    public Ticket createTicket(CreateTicketRequest request) {

        String tenantId = request.tenantId();
        String ticketId = ticketKeyGenerator.nextTicketId(tenantId);
        TicketPriority priority = Optional.ofNullable(request.priority()).orElse(TicketPriority.MEDIUM);
        String assigneeId = request.assigneeId();
        String nextActionOwnerId = request.nextActionOwnerId();

        Instant slaTimer = Instant.now().plus(15000,MINUTES);

        Ticket ticket = Ticket.builder()
                .ticketId(ticketId)
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
        return ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    @Override
    public Ticket updateTicket(String id,UpdateTicketRequest request) throws TicketNotFoundException {
        Ticket ticket = ticketRepository.findByTicketId(id)
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


    @Override
    public Ticket transitionStatusTicket(String ticketId,
                                   StatusTransitionRequest req)
            throws TicketNotFoundException, IllegalTransitionException
    {
        Ticket t = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        TicketStatus from = t.getStatus();
        TicketStatus to   = req.newStatus();

        // 1) validate move
        if (!ALLOWED.getOrDefault(from, Set.of()).contains(to)) {
            throw new IllegalTransitionException(from.name(), to.name());
        }

        // 2) apply
        t.setStatus(to);
        t.setUpdatedAt(Instant.now());
        ticketRepository.save(t);
        return t;
    }

    @Override
    public Ticket assignTicket(String ticketId,
                               AssignTicketRequest req)
            throws TicketNotFoundException
    {
        Ticket t = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // capture old values
        String oldAssignee = t.getAssigneeId();
        String oldNext     = t.getNextActionOwnerId();

        // apply
        t.setAssigneeId(req.assigneeId());
        t.setNextActionOwnerId(req.nextActionOwnerId());
        t.setUpdatedAt(Instant.now());
        ticketRepository.save(t);

        return t;
    }


    // Allowed status jumps
    private static final Map<TicketStatus, Set<TicketStatus>> ALLOWED =
            Map.of(
                    TicketStatus.NEW, Set.of(TicketStatus.IN_PROGRESS),
                    TicketStatus.IN_PROGRESS, Set.of(
                            TicketStatus.BLOCKED,
                            TicketStatus.WAITING_ON_CLIENT,
                            TicketStatus.COMPLETED),
                    TicketStatus.BLOCKED, Set.of(TicketStatus.IN_PROGRESS),
                    TicketStatus.WAITING_ON_CLIENT, Set.of(TicketStatus.IN_PROGRESS),
                    TicketStatus.COMPLETED, Set.of()  // final
            );
}
