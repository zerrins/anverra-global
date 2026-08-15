package com.anverraglobal.commission.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table("commissions")
public class CommissionEntity {

    @Id
    private UUID policyId; // Logical reference acting as PK for Commission
    private String status;
    private String type;
    private BigDecimal totalCommissionValue;
    private BigDecimal agentAShare;
    private BigDecimal agentBShare;
    
    @Version
    private Long version;

    // Getters and Setters
    public UUID getPolicyId() { return policyId; }
    public void setPolicyId(UUID policyId) { this.policyId = policyId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getTotalCommissionValue() { return totalCommissionValue; }
    public void setTotalCommissionValue(BigDecimal totalCommissionValue) { this.totalCommissionValue = totalCommissionValue; }
    public BigDecimal getAgentAShare() { return agentAShare; }
    public void setAgentAShare(BigDecimal agentAShare) { this.agentAShare = agentAShare; }
    public BigDecimal getAgentBShare() { return agentBShare; }
    public void setAgentBShare(BigDecimal agentBShare) { this.agentBShare = agentBShare; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
