package com.anverraglobal.policy.adapter.outbound.persistence;

import com.anverraglobal.policy.application.port.outbound.PolicyDocumentRepositoryPort;
import com.anverraglobal.policy.domain.PolicyDocument;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PolicyDocumentPersistenceAdapter implements PolicyDocumentRepositoryPort {

    private final PolicyDocumentRepository policyDocumentRepository;

    public PolicyDocumentPersistenceAdapter(PolicyDocumentRepository policyDocumentRepository) {
        this.policyDocumentRepository = policyDocumentRepository;
    }

    @Override
    public PolicyDocument save(PolicyDocument document) {
        PolicyDocumentEntity entity = new PolicyDocumentEntity();
        entity.setPolicyId(document.getPolicyId());
        entity.setStorageKey(document.getStorageKey());
        entity.setOriginalFilename(document.getOriginalFilename());
        entity.setContentType(document.getContentType());
        entity.setSizeBytes(document.getSizeBytes());
        entity.setVersion(document.getVersion());

        PolicyDocumentEntity saved = policyDocumentRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<PolicyDocument> findByPolicyId(UUID policyId) {
        return policyDocumentRepository.findById(policyId).map(this::mapToDomain);
    }

    @Override
    public void deleteByPolicyId(UUID policyId) {
        policyDocumentRepository.deleteById(policyId);
    }

    private PolicyDocument mapToDomain(PolicyDocumentEntity entity) {
        return new PolicyDocument(
                entity.getPolicyId(),
                entity.getStorageKey(),
                entity.getOriginalFilename(),
                entity.getContentType(),
                entity.getSizeBytes(),
                entity.getVersion()
        );
    }
}
