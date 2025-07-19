package com.ticket_management_system.auth_service.auth_service.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean("ticketsCreatedCounter")
    public Counter ticketsCreatedCounter(MeterRegistry registry) {
        return Counter.builder("tickets_created_total")
                      .description("Total tickets created")
                      .register(registry);
    }
}