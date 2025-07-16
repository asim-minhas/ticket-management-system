package com.ticket_management_system.ticketing_service.repositories;

import com.ticket_management_system.ticketing_service.model.TicketHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketHistoryRepository extends MongoRepository<TicketHistory,String> {
    List<TicketHistory> findByTicketIdOrderByChangedAtAsc(String ticketId);
}
