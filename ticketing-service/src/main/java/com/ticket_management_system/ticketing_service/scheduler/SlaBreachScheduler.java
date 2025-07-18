package com.ticket_management_system.ticketing_service.scheduler;

import com.ticket_management_system.ticketing_service.events.SlaBreachedEvent;
import com.ticket_management_system.ticketing_service.model.Ticket;
import com.ticket_management_system.ticketing_service.model.TicketHistory;
import com.ticket_management_system.ticketing_service.repositories.TicketHistoryRepository;
import com.ticket_management_system.ticketing_service.repositories.TicketRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
@Component
public class SlaBreachScheduler {

    private final TicketRepository        ticketRepo;
    private final TicketHistoryRepository historyRepo;
    private final ApplicationEventPublisher publisher;

    public SlaBreachScheduler(TicketRepository ticketRepo,
                              TicketHistoryRepository historyRepo,
                              ApplicationEventPublisher publisher) {
        this.ticketRepo   = ticketRepo;
        this.historyRepo  = historyRepo;
        this.publisher    = publisher;
    }

    /**
     * Runs every day at 00:00 server time.
     * Flags any tickets whose dueAt is past and not yet breached.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void checkForBreachesDaily() {
        Instant now = Instant.now();
        // all tickets past due & not yet breached
        var due = ticketRepo.findByDueAtBeforeAndBreachedFalse(now);

        for (Ticket t : due) {
            t.setBreached(true);
            ticketRepo.save(t);

            long daysOverdue = Duration.between(t.getDueAt(), now)
                    .toDays();

            TicketHistory hist = TicketHistory.builder()
                    .ticketId(  t.getId()            )
                    .fromStatus(t.getStatus()        )
                    .toStatus(  t.getStatus()        )
                    .changedBy("system")
                    .changedAt(now)
                    .comment("SLA breached after "
                            + daysOverdue
                            + (daysOverdue == 1 ? " day" : " days"))
                    .build();
            historyRepo.save(hist);

            publisher.publishEvent(
                    new SlaBreachedEvent(t.getId(), now)
            );
        }
    }
}
