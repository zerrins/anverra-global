package com.anverraglobal.reporting.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("reporting_policy_read_models")
public class ReportingReadModelEntity {

    @Id
    private UUID policyId;
    private String policyNumber;
    private UUID customerId;
    private UUID productId;
    private UUID agentAId;
    private UUID agentBId;
    private UUID branchId;
    private BigDecimal premium;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal sumAssured;
    private String status;
    private String commissionStatus;
    private String commissionType;
    private BigDecimal totalCommissionValue;
    private BigDecimal agentAShare;
    private BigDecimal agentBShare;
    private Long policyAggregateVersion;
    private Long commissionAggregateVersion;

    public UUID getPolicyId() { return policyId; }
    public void setPolicyId(UUID policyId) { this.policyId = policyId; }
    public String getPolicyNumber() { return policyNumber; }
    public void setPolicyNumber(String policyNumber) { this.policyNumber = policyNumber; }
    public UUID getCustomerId() { return customerId; }
    public void setCustomerId(UUID customerId) { this.customerId = customerId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public UUID getAgentAId() { return agentAId; }
    public void setAgentAId(UUID agentAId) { this.agentAId = agentAId; }
    public UUID getAgentBId() { return agentBId; }
    public void setAgentBId(UUID agentBId) { this.agentBId = agentBId; }
    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }
    public BigDecimal getPremium() { return premium; }
    public void setPremium(BigDecimal premium) { this.premium = premium; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public BigDecimal getSumAssured() { return sumAssured; }
    public void setSumAssured(BigDecimal sumAssured) { this.sumAssured = sumAssured; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCommissionStatus() { return commissionStatus; }
    public void setCommissionStatus(String commissionStatus) { this.commissionStatus = commissionStatus; }
    public String getCommissionType() { return commissionType; }
    public void setCommissionType(String commissionType) { this.commissionType = commissionType; }
    public BigDecimal getTotalCommissionValue() { return totalCommissionValue; }
    public void setTotalCommissionValue(BigDecimal totalCommissionValue) { this.totalCommissionValue = totalCommissionValue; }
    public BigDecimal getAgentAShare() { return agentAShare; }
    public void setAgentAShare(BigDecimal agentAShare) { this.agentAShare = agentAShare; }
    public BigDecimal getAgentBShare() { return agentBShare; }
    public void setAgentBShare(BigDecimal agentBShare) { this.agentBShare = agentBShare; }
    public Long getPolicyAggregateVersion() { return policyAggregateVersion; }
    public void setPolicyAggregateVersion(Long policyAggregateVersion) { this.policyAggregateVersion = policyAggregateVersion; }
    public Long getCommissionAggregateVersion() { return commissionAggregateVersion; }
    public void setCommissionAggregateVersion(Long commissionAggregateVersion) { this.commissionAggregateVersion = commissionAggregateVersion; }
}
