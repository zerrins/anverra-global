package com.anverraglobal.organization.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;
import java.util.List;

public interface DealerRepository extends CrudRepository<DealerRecord, UUID> {
    List<DealerRecord> findAll();
}
