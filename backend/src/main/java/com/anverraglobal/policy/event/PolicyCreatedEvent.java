package com.anverraglobal.policy.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PolicyCreatedEvent(
        UUID eventId,
        Integer schemaVersion,
        Instant occurredAt,
        UUID aggregateId,
        Long aggregateVersion,
        String policyNumber,
        UUID customerId,
        UUID agentAId,
        UUID agentBId,
        UUID branchId,
        String policyStatus,
        BigDecimal premiumAmount
) {
    public static PolicyCreatedEvent create(
            UUID policyId, Long aggregateVersion, String policyNumber, 
            UUID customerId, UUID agentAId, UUID agentBId, UUID branchId, 
            String policyStatus, BigDecimal premiumAmount) {
        return new PolicyCreatedEvent(
                UUID.randomUUID(),
                1,
                Instant.now(),
                policyId,
                aggregateVersion,
                policyNumber,
                customerId,
                agentAId,
                agentBId,
                branchId,
                policyStatus,
                premiumAmount
        );
    }
}
