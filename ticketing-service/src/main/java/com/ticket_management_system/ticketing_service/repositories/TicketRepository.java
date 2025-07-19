package com.ticket_management_system.ticketing_service.repositories;

import com.ticket_management_system.ticketing_service.exceptions.TicketNotFoundException;
import com.ticket_management_system.ticketing_service.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends MongoRepository<Ticket,String> {
    Optional<Ticket> findByTicketId(String ticketId) throws TicketNotFoundException;
    List<Ticket> findByDueAtBeforeAndBreachedFalse(Instant now);
}
