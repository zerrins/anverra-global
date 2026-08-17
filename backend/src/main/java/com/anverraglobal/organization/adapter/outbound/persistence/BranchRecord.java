package com.anverraglobal.organization.adapter.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("branches")
public class BranchRecord {

    @Id
    private UUID id;
    private UUID dealerId;
    private String name;
    private String status;

    @Version
    private Long version;

    public BranchRecord() {
    }

    public BranchRecord(UUID id, UUID dealerId, String name, String status) {
        this.id = id;
        this.dealerId = dealerId;
        this.name = name;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDealerId() {
        return dealerId;
    }

    public void setDealerId(UUID dealerId) {
        this.dealerId = dealerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
