package com.anverraglobal.policy.adapter.outbound.persistence;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.policy.domain.Policy;
import com.anverraglobal.policy.domain.PolicyStatus;
import com.anverraglobal.policy.application.port.outbound.PolicyRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PolicyPersistenceAdapter implements PolicyRepositoryPort {

    private final PolicyRepository policyRepository;

    public PolicyPersistenceAdapter(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public Policy save(Policy policy) {
        PolicyEntity entity = new PolicyEntity();
        entity.setId(policy.getPolicyId());
        entity.setPolicyNumber(policy.getPolicyNumber());
        entity.setCreatedBy(policy.getCreatedBy());
        entity.setCreatedAt(policy.getCreatedAt());
        entity.setCustomerId(policy.getCustomerId());
        entity.setInsurerId(policy.getInsurerId());
        entity.setAgentAId(policy.getAgentAId());
        entity.setAgentBId(policy.getAgentBId());
        entity.setBranchId(policy.getBranchId());
        entity.setPremium(policy.getPremium());
        entity.setStatus(policy.getStatus().name());
        entity.setVersion(policy.getVersion());

        PolicyEntity saved = policyRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Policy> findById(UUID id) {
        return policyRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public Optional<Policy> findByIdAndScope(UUID id, OrganizationScope scope) {
        if (scope.isGlobalAdmin()) {
            return policyRepository.findById(id).map(this::mapToDomain);
        } else if (!scope.allowedCustomerIds().isEmpty()) {
            return policyRepository.findByIdAndCustomerIdIn(id, scope.allowedCustomerIds()).map(this::mapToDomain);
        } else if (!scope.allowedAgentIds().isEmpty()) {
            return policyRepository.findByIdAndAgentIdsIn(id, scope.allowedAgentIds()).map(this::mapToDomain);
        } else if (!scope.allowedBranchIds().isEmpty()) {
            return policyRepository.findByIdAndBranchIdIn(id, scope.allowedBranchIds()).map(this::mapToDomain);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Policy> findByPolicyNumberAndScope(String policyNumber, OrganizationScope scope) {
        if (scope.isGlobalAdmin()) {
            return policyRepository.findByPolicyNumber(policyNumber).map(this::mapToDomain);
        } else if (!scope.allowedCustomerIds().isEmpty()) {
            return policyRepository.findByPolicyNumberAndCustomerIdIn(policyNumber, scope.allowedCustomerIds()).map(this::mapToDomain);
        } else if (!scope.allowedAgentIds().isEmpty()) {
            return policyRepository.findByPolicyNumberAndAgentIdsIn(policyNumber, scope.allowedAgentIds()).map(this::mapToDomain);
        } else if (!scope.allowedBranchIds().isEmpty()) {
            return policyRepository.findByPolicyNumberAndBranchIdIn(policyNumber, scope.allowedBranchIds()).map(this::mapToDomain);
        }
        return Optional.empty();
    }

    @Override
    public Page<Policy> listByScope(OrganizationScope scope, Pageable pageable) {
        if (scope.isGlobalAdmin()) {
            return policyRepository.findAll(pageable).map(this::mapToDomain);
        } else if (!scope.allowedCustomerIds().isEmpty()) {
            return policyRepository.findByCustomerIdIn(scope.allowedCustomerIds(), pageable).map(this::mapToDomain);
        } else if (!scope.allowedAgentIds().isEmpty()) {
            return policyRepository.findByAgentAIdInOrAgentBIdIn(scope.allowedAgentIds(), scope.allowedAgentIds(), pageable).map(this::mapToDomain);
        } else if (!scope.allowedBranchIds().isEmpty()) {
            return policyRepository.findByBranchIdIn(scope.allowedBranchIds(), pageable).map(this::mapToDomain);
        }
        return Page.empty(pageable);
    }

    @Override
    public boolean existsById(UUID id) {
        return policyRepository.existsById(id);
    }

    @Override
    public boolean existsByPolicyNumber(String policyNumber) {
        return policyRepository.existsByPolicyNumber(policyNumber);
    }

    private Policy mapToDomain(PolicyEntity entity) {
        return new Policy(
                entity.getId(),
                entity.getPolicyNumber(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getCustomerId(),
                entity.getInsurerId(),
                entity.getAgentAId(),
                entity.getAgentBId(),
                entity.getBranchId(),
                entity.getPremium(),
                PolicyStatus.valueOf(entity.getStatus()),
                entity.getVersion()
        );
    }
}
