package com.anverraglobal.policy.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PolicyPremiumUpdatedEvent(
        UUID eventId,
        Integer schemaVersion,
        Instant occurredAt,
        UUID aggregateId,
        Long aggregateVersion,
        String policyNumber,
        UUID customerId,
        UUID productId,
        UUID agentAId,
        UUID agentBId,
        UUID branchId,
        String policyStatus,
        BigDecimal premiumAmount,
        LocalDate effectiveDate,
        LocalDate expiryDate,
        BigDecimal sumAssured
) {
    public static PolicyPremiumUpdatedEvent create(
            UUID policyId, Long aggregateVersion, String policyNumber,
            UUID customerId, UUID productId,
            UUID agentAId, UUID agentBId, UUID branchId,
            String policyStatus, BigDecimal premiumAmount,
            LocalDate effectiveDate, LocalDate expiryDate, BigDecimal sumAssured) {
        return new PolicyPremiumUpdatedEvent(
                UUID.randomUUID(),
                1,
                Instant.now(),
                policyId,
                aggregateVersion,
                policyNumber,
                customerId,
                productId,
                agentAId,
                agentBId,
                branchId,
                policyStatus,
                premiumAmount,
                effectiveDate,
                expiryDate,
                sumAssured
        );
    }
}
