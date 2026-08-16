package com.anverraglobal.customer.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import org.postgresql.util.PGobject;

import java.time.Instant;
import java.util.UUID;

@Table("customers")
public class CustomerEntity {

    @Id
    private UUID id;
    private String customerType;
    private String name;
    private String contactInfo;
    private String addressInfo;
    private String status;
    private PGobject individualInfo;
    private PGobject businessInfo;
    private UUID dealerId;
    private UUID branchId;
    private UUID agentId;
    private Instant createdAt;
    private Instant updatedAt;
    
    @Version
    private Long version;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCustomerType() { return customerType; }
    public void setCustomerType(String customerType) { this.customerType = customerType; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getAddressInfo() { return addressInfo; }
    public void setAddressInfo(String addressInfo) { this.addressInfo = addressInfo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public PGobject getIndividualInfo() { return individualInfo; }
    public void setIndividualInfo(PGobject individualInfo) { this.individualInfo = individualInfo; }

    public PGobject getBusinessInfo() { return businessInfo; }
    public void setBusinessInfo(PGobject businessInfo) { this.businessInfo = businessInfo; }

    public UUID getDealerId() { return dealerId; }
    public void setDealerId(UUID dealerId) { this.dealerId = dealerId; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public UUID getAgentId() { return agentId; }
    public void setAgentId(UUID agentId) { this.agentId = agentId; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
