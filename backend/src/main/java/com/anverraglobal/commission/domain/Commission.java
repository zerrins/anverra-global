package com.anverraglobal.commission.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class Commission {

    private UUID policyId; // This is the aggregate identity
    private CommissionStatus status;
    private CommissionType type;
    private BigDecimal totalCommissionValue;
    private BigDecimal agentAShare;
    private BigDecimal agentBShare;
    private Long version;

    public Commission(UUID policyId, CommissionStatus status, CommissionType type, 
                      BigDecimal totalCommissionValue, BigDecimal agentAShare, 
                      BigDecimal agentBShare, Long version) {
        this.policyId = policyId;
        this.status = status;
        this.type = type;
        this.totalCommissionValue = totalCommissionValue;
        this.agentAShare = agentAShare;
        this.agentBShare = agentBShare;
        this.version = version;
    }

    public static Commission createUnset(UUID policyId) {
        return new Commission(policyId, CommissionStatus.UNSET, null, null, null, null, null);
    }

    public void resetToUnset() {
        this.status = CommissionStatus.UNSET;
        this.type = null;
        this.totalCommissionValue = null;
        this.agentAShare = null;
        this.agentBShare = null;
    }

    public void configure(CommissionType type, BigDecimal totalValue, BigDecimal agentAShare, BigDecimal agentBShare, BigDecimal policyPremium) {
        if (policyPremium == null || policyPremium.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid policy premium provided for validation");
        }
        
        if (totalValue == null || totalValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total commission must be zero or positive");
        }

        // Validate 50% maximum limit
        BigDecimal maxAllowed = policyPremium.multiply(new BigDecimal("0.50"));
        if (totalValue.compareTo(maxAllowed) > 0) {
            throw new IllegalStateException("Total commission cannot exceed 50% of the policy premium");
        }

        // Validate allocation total matches
        BigDecimal shareSum = (agentAShare != null ? agentAShare : BigDecimal.ZERO)
                .add(agentBShare != null ? agentBShare : BigDecimal.ZERO);
                
        if (shareSum.compareTo(totalValue) != 0) {
            throw new IllegalStateException("Agent shares must sum exactly to the total commission value");
        }

        this.status = CommissionStatus.CONFIGURED;
        this.type = type;
        this.totalCommissionValue = totalValue;
        this.agentAShare = agentAShare;
        this.agentBShare = agentBShare;
    }

    public UUID getPolicyId() { return policyId; }
    public CommissionStatus getStatus() { return status; }
    public CommissionType getType() { return type; }
    public BigDecimal getTotalCommissionValue() { return totalCommissionValue; }
    public BigDecimal getAgentAShare() { return agentAShare; }
    public BigDecimal getAgentBShare() { return agentBShare; }
    public Long getVersion() { return version; }
}
