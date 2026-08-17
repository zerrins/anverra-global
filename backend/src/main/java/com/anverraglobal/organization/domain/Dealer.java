package com.anverraglobal.organization.domain;

import java.util.UUID;

public class Dealer {

    private UUID id;
    private String name;
    private OrganizationStatus status;
    private Long version;

    public Dealer(UUID id, String name, OrganizationStatus status, Long version) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        this.id = id;
        this.name = name;
        this.status = status;
        this.version = version;
    }

    public static Dealer create(String name) {
        return new Dealer(UUID.randomUUID(), name, OrganizationStatus.ACTIVE, 0L);
    }

    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        this.name = newName;
    }

    public void activate() {
        if (this.status == OrganizationStatus.ACTIVE) {
            throw new IllegalStateException("Dealer is already ACTIVE");
        }
        this.status = OrganizationStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status == OrganizationStatus.INACTIVE) {
            throw new IllegalStateException("Dealer is already INACTIVE");
        }
        this.status = OrganizationStatus.INACTIVE;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }
}
