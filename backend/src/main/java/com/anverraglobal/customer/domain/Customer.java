package com.anverraglobal.customer.domain;

import java.time.Instant;
import java.util.UUID;

public class Customer {

    private final UUID id;
    private final CustomerType customerType;
    private String name;
    private String contactInfo;
    private String addressInfo;
    private CustomerStatus status;
    private String individualInfo;
    private String businessInfo;
    
    private final UUID dealerId;
    private final UUID branchId;
    private final UUID agentId;
    
    private final Instant createdAt;
    private Instant updatedAt;
    private final Long version;

    public Customer(UUID id, CustomerType customerType, String name, String contactInfo, String addressInfo,
                    CustomerStatus status, String individualInfo, String businessInfo, 
                    UUID dealerId, UUID branchId, UUID agentId, 
                    Instant createdAt, Instant updatedAt, Long version) {
        
        if (id == null) throw new IllegalArgumentException("id must not be null");
        if (customerType == null) throw new IllegalArgumentException("customerType must not be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (contactInfo == null || contactInfo.isBlank()) throw new IllegalArgumentException("contactInfo must not be blank");
        if (addressInfo == null || addressInfo.isBlank()) throw new IllegalArgumentException("addressInfo must not be blank");
        if (dealerId == null) throw new IllegalArgumentException("dealerId must not be null");
        if (branchId == null) throw new IllegalArgumentException("branchId must not be null");
        if (agentId == null) throw new IllegalArgumentException("agentId must not be null");

        if (customerType == CustomerType.INDIVIDUAL) {
            if (individualInfo == null || individualInfo.isBlank()) {
                throw new IllegalArgumentException("individualInfo is required for INDIVIDUAL customers");
            }
        }
        
        if (customerType == CustomerType.ORGANIZATION) {
            if (businessInfo == null || businessInfo.isBlank()) {
                throw new IllegalArgumentException("businessInfo is required for ORGANIZATION customers");
            }
        }

        this.id = id;
        this.customerType = customerType;
        this.name = name;
        this.contactInfo = contactInfo;
        this.addressInfo = addressInfo;
        this.status = status;
        this.individualInfo = individualInfo;
        this.businessInfo = businessInfo;
        this.dealerId = dealerId;
        this.branchId = branchId;
        this.agentId = agentId;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        this.version = version;
    }

    public static Customer create(CustomerType customerType, String name, String contactInfo, String addressInfo, 
                                  String individualInfo, String businessInfo, 
                                  UUID dealerId, UUID branchId, UUID agentId) {
        return new Customer(
                UUID.randomUUID(), 
                customerType, 
                name, 
                contactInfo, 
                addressInfo, 
                CustomerStatus.ACTIVE, 
                individualInfo, 
                businessInfo, 
                dealerId, 
                branchId, 
                agentId, 
                Instant.now(), 
                Instant.now(), 
                null
        );
    }

    public void update(String name, String contactInfo, String addressInfo, String individualInfo, String businessInfo) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (contactInfo == null || contactInfo.isBlank()) throw new IllegalArgumentException("contactInfo must not be blank");
        if (addressInfo == null || addressInfo.isBlank()) throw new IllegalArgumentException("addressInfo must not be blank");
        
        if (this.customerType == CustomerType.INDIVIDUAL) {
            if (individualInfo == null || individualInfo.isBlank()) {
                throw new IllegalArgumentException("individualInfo is required for INDIVIDUAL customers");
            }
            this.individualInfo = individualInfo;
        }
        
        if (this.customerType == CustomerType.ORGANIZATION) {
            if (businessInfo == null || businessInfo.isBlank()) {
                throw new IllegalArgumentException("businessInfo is required for ORGANIZATION customers");
            }
            this.businessInfo = businessInfo;
        }
        
        this.name = name;
        this.contactInfo = contactInfo;
        this.addressInfo = addressInfo;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.status != CustomerStatus.ACTIVE) {
            this.status = CustomerStatus.ACTIVE;
            this.updatedAt = Instant.now();
        }
    }

    public void deactivate() {
        if (this.status != CustomerStatus.INACTIVE) {
            this.status = CustomerStatus.INACTIVE;
            this.updatedAt = Instant.now();
        }
    }

    public UUID getId() { return id; }
    public CustomerType getCustomerType() { return customerType; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }
    public String getAddressInfo() { return addressInfo; }
    public CustomerStatus getStatus() { return status; }
    public String getIndividualInfo() { return individualInfo; }
    public String getBusinessInfo() { return businessInfo; }
    public UUID getDealerId() { return dealerId; }
    public UUID getBranchId() { return branchId; }
    public UUID getAgentId() { return agentId; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }
}
