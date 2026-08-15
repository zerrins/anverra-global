package com.anverraglobal.commission.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CommissionConfiguredEvent(
        UUID eventId,
        Integer schemaVersion,
        Instant occurredAt,
        UUID aggregateId,
        Long aggregateVersion,
        String commissionStatus,
        String commissionType,
        BigDecimal totalCommissionValue,
        BigDecimal agentAShare,
        BigDecimal agentBShare
) {
    public static CommissionConfiguredEvent create(
            UUID policyId, Long aggregateVersion, String commissionStatus, 
            String commissionType, BigDecimal totalCommissionValue, 
            BigDecimal agentAShare, BigDecimal agentBShare) {
        return new CommissionConfiguredEvent(
                UUID.randomUUID(),
                1,
                Instant.now(),
                policyId,
                aggregateVersion,
                commissionStatus,
                commissionType,
                totalCommissionValue,
                agentAShare,
                agentBShare
        );
    }
}
