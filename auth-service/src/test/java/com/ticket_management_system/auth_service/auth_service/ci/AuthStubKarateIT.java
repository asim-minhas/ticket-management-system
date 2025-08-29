package com.ticket_management_system.auth_service.auth_service.ci;

import com.intuit.karate.junit5.Karate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(
  classes = { CiStubApp.class, CiStubSecurity.class, CiStubController.class },
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthStubKarateIT {

  @LocalServerPort
  int port;

  @BeforeAll
  void exposeBaseUrl() {
    System.setProperty("BASE_URL", "http://127.0.0.1:" + port);
  }

  @Karate.Test
  Karate run() {
    // run all features under src/test/resources/karate
    return Karate.run("classpath:com/ticket_management_system/auth_service/auth_service/karate/auth.feature");
  }
}
