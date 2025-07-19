package com.ticket_management_system.ticketing_service.audit;

import com.ticket_management_system.ticketing_service.dto.request.AssignTicketRequest;
import com.ticket_management_system.ticketing_service.dto.request.StatusTransitionRequest;
import com.ticket_management_system.ticketing_service.model.Ticket;
import com.ticket_management_system.ticketing_service.model.TicketHistory;
import com.ticket_management_system.ticketing_service.repositories.TicketHistoryRepository;
import com.ticket_management_system.ticketing_service.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class TicketAuditAspect {

    private final TicketRepository        ticketRepository;
    private final TicketHistoryRepository historyRepo;

    /**
     * Matches any service method whose name contains "Ticket"
     */
    @Pointcut("execution(* com.ticket_management_system.ticketing_service.service.TicketService.*Ticket*(..))")
    private void anyTicketUpdate() {}

    /**
     * After any *Ticket* method returns a Ticket, decide whether to audit it.
     */
    @AfterReturning(pointcut = "anyTicketUpdate()", returning = "ticket")
    public void auditTicketChange(JoinPoint jp, Ticket ticket) {
        String method = jp.getSignature().getName();

        // 1) Skip creation & read-only
        switch (method) {
            case "createTicket":
            case "getTicketById":
            case "listTickets":
                return;
        }

        // 2) Load "before" state
        Optional<Ticket> beforeOpt = ticketRepository.findById(ticket.getId());
        if (beforeOpt.isEmpty()) return;
        Ticket before = beforeOpt.get();

        // 3) Who made the change
        String user = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        TicketHistory entry;

        // 4) Build a method-specific history entry
        switch (method) {
            case "updateTicket" -> {
                entry = TicketHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(before.getStatus())
                        .toStatus(ticket.getStatus())
                        .changedBy(user)
                        .changedAt(Instant.now())
                        .comment("Generic update via PATCH")
                        .build();
            }

            case "assignTicket" -> {
                var req = (AssignTicketRequest) jp.getArgs()[1];
                String comment = String.format(
                        "Assignee: %s → %s; NextOwner: %s → %s",
                        before.getAssigneeId(),       req.assigneeId(),
                        before.getNextActionOwnerId(), req.nextActionOwnerId()
                );
                entry = TicketHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(before.getStatus())
                        .toStatus(ticket.getStatus())
                        .changedBy(user)
                        .changedAt(Instant.now())
                        .comment(comment)
                        .build();
            }

            case "transistionstatusTicket" -> {
                var req = (StatusTransitionRequest) jp.getArgs()[1];
                entry = TicketHistory.builder()
                        .ticketId(ticket.getId())
                        .fromStatus(before.getStatus())
                        .toStatus(ticket.getStatus())
                        .changedBy(user)
                        .changedAt(Instant.now())
                        .comment(req.comment())
                        .build();
            }

            default -> {
                // If you add new *Ticket* methods, you can either
                //   • return;           // to skip auditing
                //   • or build an entry  // to capture them.
                return;
            }
        }

        // 5) Persist the audit record
        historyRepo.save(entry);
    }
}
