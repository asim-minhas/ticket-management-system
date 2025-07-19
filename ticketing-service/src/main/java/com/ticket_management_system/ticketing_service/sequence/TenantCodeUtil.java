package com.ticket_management_system.ticketing_service.sequence;

import org.springframework.stereotype.Component;

@Component
public class TenantCodeUtil {

    /**
     * Takes the first three characters of the tenantId and upper-cases them.
     * @param tenantId the raw tenant identifier
     * @return a 3-letter uppercase code, e.g. "F47" from "f47ac10b-…"
     */
    public String deriveCode(String tenantId) {
        if (tenantId == null || tenantId.length() < 3) {
            throw new IllegalArgumentException(
                "tenantId must be at least 3 characters"
            );
        }
        return tenantId.substring(0, 3).toUpperCase();
    }
}