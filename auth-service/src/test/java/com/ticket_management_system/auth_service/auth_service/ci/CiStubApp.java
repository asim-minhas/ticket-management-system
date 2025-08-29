package com.ticket_management_system.auth_service.auth_service.ci;

import org.springframework.boot.autoconfigure.SpringBootApplication;

// Scan ONLY the ci package so none of your real Mongo/JWT beans load
@SpringBootApplication(scanBasePackageClasses = CiStubApp.class)
public class CiStubApp { }
