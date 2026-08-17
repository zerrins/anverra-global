package com.anverraglobal.policy.application.port.outbound;

import com.anverraglobal.policy.domain.PolicyDocument;

import java.util.Optional;
import java.util.UUID;

public interface PolicyDocumentRepositoryPort {
    PolicyDocument save(PolicyDocument document);
    Optional<PolicyDocument> findByPolicyId(UUID policyId);
    void deleteByPolicyId(UUID policyId);
}
