package com.ticket_management_system.ticketing_service.repositories;

import com.ticket_management_system.ticketing_service.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket,String> {
    Ticket getTicketById(String id);
}
