package com.anverraglobal.policy.application.port.outbound;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.policy.domain.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PolicyRepositoryPort {
    
    Policy save(Policy policy);
    
    Optional<Policy> findById(UUID id);
    
    Optional<Policy> findByIdAndScope(UUID id, OrganizationScope scope);
    
    Optional<Policy> findByPolicyNumberAndScope(String policyNumber, OrganizationScope scope);
    
    Page<Policy> listByScope(OrganizationScope scope, Pageable pageable);
    
    boolean existsById(UUID id);
    
    boolean existsByPolicyNumber(String policyNumber);
}
