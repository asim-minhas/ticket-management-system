package com.ticket_management_system.auth_service.auth_service.karate;
import com.intuit.karate.junit5.Karate;

public class AuthKarateRunner {
    @Karate.Test
    Karate run() {
        return Karate.run("auth").relativeTo(getClass());
    }
}
