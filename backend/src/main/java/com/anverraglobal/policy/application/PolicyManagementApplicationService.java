package com.anverraglobal.policy.application;

import com.anverraglobal.commission.contracts.CommissionManagementService;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.policy.domain.Policy;
import com.anverraglobal.policy.domain.PolicyStatus;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;
import com.anverraglobal.policy.application.port.outbound.PolicyRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyManagementApplicationService {

    private final CommissionManagementService commissionManagementService;
    private final PolicyRepositoryPort policyRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationScopeResolutionService scopeResolutionService;

    public PolicyManagementApplicationService(CommissionManagementService commissionManagementService,
                                              PolicyRepositoryPort policyRepositoryPort,
                                              ApplicationEventPublisher eventPublisher,
                                              OrganizationScopeResolutionService scopeResolutionService) {
        this.commissionManagementService = commissionManagementService;
        this.policyRepositoryPort = policyRepositoryPort;
        this.eventPublisher = eventPublisher;
        this.scopeResolutionService = scopeResolutionService;
    }

    @Transactional
    public Policy createPolicy(UUID identityId, String role, String policyNumber, UUID customerId, UUID agentAId, UUID agentBId, UUID branchId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        
        Policy policy = Policy.createDraft(policyNumber, identityId, customerId, agentAId, agentBId, branchId);
        
        assertScope(scope, policy);

        Policy saved = policyRepositoryPort.save(policy);
        
        PolicyCreatedEvent event = PolicyCreatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(), 
                saved.getCustomerId(), saved.getAgentAId(), saved.getAgentBId(), 
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium());
        eventPublisher.publishEvent(event);
        
        return saved;
    }

    @Transactional(readOnly = true)
    public Policy getPolicy(UUID identityId, String role, UUID policyId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        return getScopedPolicy(policyId, scope);
    }

    @Transactional(readOnly = true)
    public Page<Policy> listPolicies(UUID identityId, String role, Pageable pageable) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        return policyRepositoryPort.listByScope(scope, pageable);
    }

    @Transactional
    public Policy updatePolicy(UUID identityId, String role, UUID policyId, UUID customerId, UUID agentAId, UUID agentBId, UUID branchId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);
        
        // Wait, domain model has no direct setters for customerId, agentAId, etc in the previous snippet, 
        // Let's create a new Policy instance with updated fields or add setters if they existed.
        // Actually, let's look at Policy.java. It doesn't have setters. We need to create a new instance with the same ID and Version.
        Policy updatedPolicy = new Policy(
            policy.getPolicyId(),
            policy.getPolicyNumber(),
            policy.getCreatedBy(),
            policy.getCreatedAt(),
            customerId != null ? customerId : policy.getCustomerId(),
            agentAId != null ? agentAId : policy.getAgentAId(),
            agentBId != null ? agentBId : policy.getAgentBId(),
            branchId != null ? branchId : policy.getBranchId(),
            policy.getPremium(),
            policy.getStatus(),
            policy.getVersion()
        );
        
        assertScope(scope, updatedPolicy); // ensure they didn't move it out of their scope
        
        return policyRepositoryPort.save(updatedPolicy);
    }

    @Transactional(readOnly = true)
    public UUID resolvePolicy(UUID identityId, String role, String policyNumber) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicyByNumber(policyNumber, scope);
        return policy.getPolicyId();
    }

    @Transactional
    public void activatePolicy(UUID identityId, String role, UUID policyId, boolean isCommissionConfigured) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);

        boolean authoritativeCommissionStatus = commissionManagementService.isCommissionConfigured(policyId);
        policy.activate(authoritativeCommissionStatus);
        
        Policy saved = policyRepositoryPort.save(policy);
        
        PolicyActivatedEvent event = PolicyActivatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(), 
                saved.getCustomerId(), saved.getAgentAId(), saved.getAgentBId(), 
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void deactivatePolicy(UUID identityId, String role, UUID policyId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);

        policy.deactivate();
        
        Policy saved = policyRepositoryPort.save(policy);
        
        PolicyDeactivatedEvent event = PolicyDeactivatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(), 
                saved.getCustomerId(), saved.getAgentAId(), saved.getAgentBId(), 
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void reactivatePolicy(UUID identityId, String role, UUID policyId, boolean isCommissionConfigured) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);

        boolean authoritativeCommissionStatus = commissionManagementService.isCommissionConfigured(policyId);
        policy.activate(authoritativeCommissionStatus);
        
        Policy saved = policyRepositoryPort.save(policy);
        
        PolicyReactivatedEvent event = PolicyReactivatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(), 
                saved.getCustomerId(), saved.getAgentAId(), saved.getAgentBId(), 
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void updatePremium(UUID policyId, BigDecimal newPremium) {
        Policy policy = policyRepositoryPort.findById(policyId)
                .orElseThrow(() -> new NoSuchElementException("Policy not found: " + policyId));
        
        policy.updatePremium(newPremium);
        
        Policy saved = policyRepositoryPort.save(policy);

        commissionManagementService.resetToUnset(policyId);

        PolicyPremiumUpdatedEvent event = PolicyPremiumUpdatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(), 
                saved.getCustomerId(), saved.getAgentAId(), saved.getAgentBId(), 
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium());
        
        eventPublisher.publishEvent(event);
    }

    public void configureCommission(UUID identityId, String role, UUID policyId, String commissionType, BigDecimal totalCommissionValue, BigDecimal agentAShare, BigDecimal agentBShare) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);
        
        commissionManagementService.configureCommission(
                policyId, 
                commissionType, 
                totalCommissionValue, 
                agentAShare, 
                agentBShare, 
                policy.getPremium()
        );
    }

    private void assertScope(OrganizationScope scope, Policy policy) {
        if (scope.isGlobalAdmin()) return;
        if (policy.getCustomerId() != null && scope.allowsCustomer(policy.getCustomerId())) return;
        if (policy.getAgentAId() != null && scope.allowsAgent(policy.getAgentAId())) return;
        if (policy.getAgentBId() != null && scope.allowsAgent(policy.getAgentBId())) return;
        if (policy.getBranchId() != null && scope.allowsBranch(policy.getBranchId())) return;
        throw new AccessDeniedException("Unauthorized access to Policy");
    }

    private Policy getScopedPolicy(UUID policyId, OrganizationScope scope) {
        Optional<Policy> policyOpt = policyRepositoryPort.findByIdAndScope(policyId, scope);

        if (policyOpt.isEmpty()) {
            if (policyRepositoryPort.existsById(policyId)) {
                throw new AccessDeniedException("Unauthorized access to Policy");
            }
            throw new NoSuchElementException("Policy not found: " + policyId);
        }
        return policyOpt.get();
    }

    private Policy getScopedPolicyByNumber(String policyNumber, OrganizationScope scope) {
        Optional<Policy> policyOpt = policyRepositoryPort.findByPolicyNumberAndScope(policyNumber, scope);

        if (policyOpt.isEmpty()) {
            if (policyRepositoryPort.existsByPolicyNumber(policyNumber)) {
                throw new AccessDeniedException("Unauthorized access to Policy");
            }
            throw new NoSuchElementException("Policy not found: " + policyNumber);
        }
        return policyOpt.get();
    }
}
