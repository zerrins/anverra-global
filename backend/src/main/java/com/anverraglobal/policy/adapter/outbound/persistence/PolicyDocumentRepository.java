package com.anverraglobal.policy.adapter.outbound.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PolicyDocumentRepository extends CrudRepository<PolicyDocumentEntity, UUID> {
}
