package com.anverraglobal.identity.domain;

import java.time.Instant;
import java.util.UUID;

public class IdentityProfile {

    private final UUID identityId;
    private final String displayName;
    private final String email;
    private final Instant createdAt;
    private final Instant updatedAt;

    public IdentityProfile(UUID identityId, String displayName, String email, Instant createdAt, Instant updatedAt) {
        if (identityId == null) {
            throw new IllegalArgumentException("identityId must not be null");
        }
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
        if (displayName.length() > 255) {
            throw new IllegalArgumentException("displayName exceeds maximum length");
        }
        this.identityId = identityId;
        this.displayName = displayName;
        this.email = email;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
    }

    public UUID getIdentityId() {
        return identityId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
