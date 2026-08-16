package com.anverraglobal.product.domain;

import java.time.Instant;
import java.util.UUID;

public class Product {
    private UUID id;
    private String name;
    private ProductCategory category;
    private ProductStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    public Product(UUID id, String name, ProductCategory category, ProductStatus status, Instant createdAt, Instant updatedAt, Long version) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.version = version;
    }

    public static Product create(String name, ProductCategory category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        Instant now = Instant.now();
        return new Product(UUID.randomUUID(), name, category, ProductStatus.ACTIVE, now, now, null);
    }

    public void update(String name, ProductCategory category) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        this.name = name;
        this.category = category;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.status == ProductStatus.ACTIVE) {
            return;
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        if (this.status == ProductStatus.INACTIVE) {
            return;
        }
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public ProductCategory getCategory() { return category; }
    public ProductStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public Long getVersion() { return version; }
}
