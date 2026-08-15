package com.anverraglobal.organization.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("organization_memberships")
public class OrganizationMembershipRecord {

    @Id
    private UUID id;
    private UUID identityId;
    private String role;
    private UUID branchId;
    private UUID dealerId;
    private UUID parentIdentityId;

    @Version
    private Long version;

    public OrganizationMembershipRecord() {
    }

    public OrganizationMembershipRecord(UUID id, UUID identityId, String role, UUID branchId, UUID dealerId, UUID parentIdentityId) {
        this.id = id;
        this.identityId = identityId;
        this.role = role;
        this.branchId = branchId;
        this.dealerId = dealerId;
        this.parentIdentityId = parentIdentityId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdentityId() {
        return identityId;
    }

    public void setIdentityId(UUID identityId) {
        this.identityId = identityId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UUID getBranchId() {
        return branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public UUID getDealerId() {
        return dealerId;
    }

    public void setDealerId(UUID dealerId) {
        this.dealerId = dealerId;
    }

    public UUID getParentIdentityId() {
        return parentIdentityId;
    }

    public void setParentIdentityId(UUID parentIdentityId) {
        this.parentIdentityId = parentIdentityId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
