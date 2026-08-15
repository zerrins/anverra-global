package com.anverraglobal.organization.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BranchRepository extends CrudRepository<BranchRecord, UUID> {
    List<BranchRecord> findByDealerId(UUID dealerId);
}
