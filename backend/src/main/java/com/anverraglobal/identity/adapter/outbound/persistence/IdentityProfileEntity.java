package com.anverraglobal.identity.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("identities")
public class IdentityProfileEntity {

    @Id
    private UUID identityId;
    private String displayName;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;

    public IdentityProfileEntity(UUID identityId, String displayName, String email, Instant createdAt, Instant updatedAt) {
        this.identityId = identityId;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getIdentityId() {
        return identityId;
    }

    public void setIdentityId(UUID identityId) {
        this.identityId = identityId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
