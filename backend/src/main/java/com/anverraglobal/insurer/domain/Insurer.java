package com.anverraglobal.insurer.domain;

import java.time.Instant;
import java.util.UUID;

public class Insurer {
    private UUID id;
    private String name;
    private InsurerStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    public Insurer(UUID id, String name, InsurerStatus status, Instant createdAt, Instant updatedAt, Long version) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static Insurer create(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        Instant now = Instant.now();
        return new Insurer(UUID.randomUUID(), name, InsurerStatus.ACTIVE, now, now, null);
    }

    public void update(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.status == InsurerStatus.ACTIVE) {
            return;
        }
        this.status = InsurerStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        if (this.status == InsurerStatus.INACTIVE) {
            return;
        }
        this.status = InsurerStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public InsurerStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }
}
