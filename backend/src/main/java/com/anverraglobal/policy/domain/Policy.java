package com.anverraglobal.policy.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Policy {

    private UUID policyId;
    private String policyNumber;
    private UUID createdBy;
    private Instant createdAt;
    private UUID customerId;
    private UUID agentAId;
    private UUID agentBId;
    private UUID branchId;
    private BigDecimal premium;
    private PolicyStatus status;
    private Long version;

    // For reconstruction from persistence
    public Policy(UUID policyId, String policyNumber, UUID createdBy, Instant createdAt, 
                  UUID customerId, UUID agentAId, UUID agentBId, UUID branchId, 
                  BigDecimal premium, PolicyStatus status, Long version) {
        this.policyId = policyId;
        this.policyNumber = policyNumber;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.agentAId = agentAId;
        this.agentBId = agentBId;
        this.branchId = branchId;
        this.premium = premium;
        this.status = status;
        this.version = version;
    }

    // Factory for new draft policy
    public static Policy createDraft(String policyNumber, UUID createdBy, UUID customerId, 
                                     UUID agentAId, UUID agentBId, UUID branchId) {
        if (agentAId == null && agentBId != null) {
            throw new IllegalArgumentException("Agent A must be populated before Agent B");
        }
        return new Policy(
            UUID.randomUUID(),
            policyNumber,
            createdBy,
            Instant.now(),
            customerId,
            agentAId,
            agentBId,
            branchId,
            BigDecimal.ZERO,
            PolicyStatus.DRAFT,
            null
        );
    }

    public void updatePremium(BigDecimal newPremium) {
        if (newPremium == null || newPremium.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Premium must be zero or positive");
        }
        this.premium = newPremium;
    }

    public void activate(boolean isCommissionConfigured) {
        if (this.status != PolicyStatus.DRAFT && this.status != PolicyStatus.INACTIVE) {
            throw new IllegalStateException("Policy must be in DRAFT or INACTIVE state to be activated");
        }
        
        int agentCount = 0;
        if (this.agentAId != null) agentCount++;
        if (this.agentBId != null) agentCount++;

        if (!isCommissionConfigured && agentCount > 0) {
            throw new IllegalStateException("Activation prohibited: Policy has agents but commission is UNSET");
        }

        this.status = PolicyStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status != PolicyStatus.ACTIVE) {
            throw new IllegalStateException("Policy must be ACTIVE to be deactivated");
        }
        this.status = PolicyStatus.INACTIVE;
    }

    // Getters
    public UUID getPolicyId() { return policyId; }
    public String getPolicyNumber() { return policyNumber; }
    public UUID getCreatedBy() { return createdBy; }
    public Instant getCreatedAt() { return createdAt; }
    public UUID getCustomerId() { return customerId; }
    public UUID getAgentAId() { return agentAId; }
    public UUID getAgentBId() { return agentBId; }
    public UUID getBranchId() { return branchId; }
    public BigDecimal getPremium() { return premium; }
    public PolicyStatus getStatus() { return status; }
    public Long getVersion() { return version; }
}
