package com.ticket_management_system.auth_service.auth_service.repositories;

import com.ticket_management_system.auth_service.auth_service.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
}
