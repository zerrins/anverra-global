package com.anverraglobal.policy.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Policy {

    private UUID policyId;
    private String policyNumber;
    private UUID createdBy;
    private Instant createdAt;
    private UUID customerId;
    private UUID insurerId;
    private UUID productId;
    private UUID agentAId;
    private UUID agentBId;
    private UUID branchId;
    private BigDecimal premium;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal sumAssured;
    private PolicyStatus status;
    private Long version;

    // For reconstruction from persistence (legacy-safe — all new fields are nullable)
    public Policy(UUID policyId, String policyNumber, UUID createdBy, Instant createdAt,
                  UUID customerId, UUID insurerId, UUID productId,
                  UUID agentAId, UUID agentBId, UUID branchId,
                  BigDecimal premium, LocalDate effectiveDate, LocalDate expiryDate,
                  BigDecimal sumAssured, PolicyStatus status, Long version) {
        this.policyId = policyId;
        this.policyNumber = policyNumber;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.insurerId = insurerId;
        this.productId = productId;
        this.agentAId = agentAId;
        this.agentBId = agentBId;
        this.branchId = branchId;
        this.premium = premium;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        this.sumAssured = sumAssured;
        this.status = status;
        this.version = version;
    }

    // Factory for new draft policy — productId required for new policies (REQ-DEC-011 §6)
    public static Policy createDraft(String policyNumber, UUID createdBy, UUID customerId,
                                     UUID insurerId, UUID productId,
                                     UUID agentAId, UUID agentBId, UUID branchId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID must not be null for new policies");
        }
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null for new policies");
        }
        if (agentAId == null && agentBId != null) {
            throw new IllegalArgumentException("Agent A must be populated before Agent B");
        }
        return new Policy(
            UUID.randomUUID(),
            policyNumber,
            createdBy,
            Instant.now(),
            customerId,
            insurerId,
            productId,
            agentAId,
            agentBId,
            branchId,
            BigDecimal.ZERO,
            null,
            null,
            null,
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

    public void updateDates(LocalDate newEffectiveDate, LocalDate newExpiryDate) {
        if (newEffectiveDate != null && newExpiryDate != null && !newEffectiveDate.isBefore(newExpiryDate)) {
            throw new IllegalArgumentException("Effective date must be before expiry date");
        }
        this.effectiveDate = newEffectiveDate;
        this.expiryDate = newExpiryDate;
    }

    public void updateSumAssured(BigDecimal newSumAssured) {
        if (newSumAssured != null && newSumAssured.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Sum assured must be zero or positive");
        }
        this.sumAssured = newSumAssured;
    }

    public void activate(boolean isCommissionConfigured) {
        if (this.status != PolicyStatus.DRAFT && this.status != PolicyStatus.INACTIVE) {
            throw new IllegalStateException("Policy must be in DRAFT or INACTIVE state to be activated");
        }

        if (this.insurerId == null) {
            throw new IllegalStateException("Policy must have an insurer to be activated");
        }

        if (this.productId == null) {
            throw new IllegalStateException("Policy must have a product to be activated");
        }

        if (this.effectiveDate == null) {
            throw new IllegalStateException("Policy must have an effective date to be activated");
        }

        if (this.expiryDate == null) {
            throw new IllegalStateException("Policy must have an expiry date to be activated");
        }

        if (!this.effectiveDate.isBefore(this.expiryDate)) {
            throw new IllegalStateException("Effective date must be before expiry date");
        }

        if (this.sumAssured == null) {
            throw new IllegalStateException("Policy must have a sum assured to be activated");
        }

        if (this.sumAssured.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException("Sum assured must be zero or positive");
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
    public UUID getInsurerId() { return insurerId; }
    public UUID getProductId() { return productId; }
    public UUID getAgentAId() { return agentAId; }
    public UUID getAgentBId() { return agentBId; }
    public UUID getBranchId() { return branchId; }
    public BigDecimal getPremium() { return premium; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public BigDecimal getSumAssured() { return sumAssured; }
    public PolicyStatus getStatus() { return status; }
    public Long getVersion() { return version; }
}
