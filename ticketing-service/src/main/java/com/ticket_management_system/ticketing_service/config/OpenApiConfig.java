package com.ticket_management_system.ticketing_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI ticketingOpenApi() {
    return new OpenAPI()
      .info(new Info()
        .title("Ticketing Service API")
        .version("v1")
        .description("REST endpoints for ticket CRUD, assignment, status transitions"))
      .externalDocs(new ExternalDocumentation()
        .description("Full spec")
        .url("/v3/api-docs"));
  }
}