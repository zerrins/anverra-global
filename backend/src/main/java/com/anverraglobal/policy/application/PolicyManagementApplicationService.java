package com.anverraglobal.policy.application;

import com.anverraglobal.commission.contracts.CommissionManagementService;
import com.anverraglobal.customer.contracts.CustomerVerificationContract;
import com.anverraglobal.insurer.contracts.InsurerVerificationContract;
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
import com.anverraglobal.product.contracts.ProductVerificationContract;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyManagementApplicationService {

    private final CommissionManagementService commissionManagementService;
    private final PolicyRepositoryPort policyRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;
    private final OrganizationScopeResolutionService scopeResolutionService;
    private final CustomerVerificationContract customerVerificationContract;
    private final InsurerVerificationContract insurerVerificationContract;
    private final ProductVerificationContract productVerificationContract;

    public PolicyManagementApplicationService(CommissionManagementService commissionManagementService,
                                              PolicyRepositoryPort policyRepositoryPort,
                                              ApplicationEventPublisher eventPublisher,
                                              OrganizationScopeResolutionService scopeResolutionService,
                                              CustomerVerificationContract customerVerificationContract,
                                              InsurerVerificationContract insurerVerificationContract,
                                              ProductVerificationContract productVerificationContract) {
        this.commissionManagementService = commissionManagementService;
        this.policyRepositoryPort = policyRepositoryPort;
        this.eventPublisher = eventPublisher;
        this.scopeResolutionService = scopeResolutionService;
        this.customerVerificationContract = customerVerificationContract;
        this.insurerVerificationContract = insurerVerificationContract;
        this.productVerificationContract = productVerificationContract;
    }

    @Transactional
    public Policy createPolicy(UUID identityId, String role, String policyNumber,
                               UUID customerId, UUID insurerId, UUID productId,
                               UUID agentAId, UUID agentBId, UUID branchId) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);

        if (customerId == null) {
            throw new IllegalArgumentException("customerId is required for new Policy");
        }
        customerVerificationContract.verifyCustomerActiveAndInScope(customerId, scope);

        if (insurerId != null) {
            insurerVerificationContract.verifyInsurerActive(insurerId);
        }

        // productId is required for new policies (REQ-DEC-011 §6)
        if (productId == null) {
            throw new IllegalArgumentException("productId is required for new Policy");
        }
        productVerificationContract.verifyProductActive(productId);

        Policy policy = Policy.createDraft(policyNumber, identityId, customerId, insurerId, productId,
                agentAId, agentBId, branchId);

        assertScope(scope, policy);

        Policy saved = policyRepositoryPort.save(policy);

        PolicyCreatedEvent event = PolicyCreatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(),
                saved.getCustomerId(), saved.getProductId(),
                saved.getAgentAId(), saved.getAgentBId(),
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium(),
                saved.getEffectiveDate(), saved.getExpiryDate(), saved.getSumAssured());
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
    public Policy updatePolicy(UUID identityId, String role, UUID policyId,
                               UUID customerId, UUID insurerId, UUID productId,
                               UUID agentAId, UUID agentBId, UUID branchId,
                               LocalDate effectiveDate, LocalDate expiryDate, BigDecimal sumAssured) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);

        UUID newCustomerId = customerId != null ? customerId : policy.getCustomerId();

        if (newCustomerId != null && !newCustomerId.equals(policy.getCustomerId())) {
            customerVerificationContract.verifyCustomerActiveAndInScope(newCustomerId, scope);
        }

        UUID newInsurerId = insurerId != null ? insurerId : policy.getInsurerId();

        if (newInsurerId != null && !newInsurerId.equals(policy.getInsurerId())) {
            insurerVerificationContract.verifyInsurerActive(newInsurerId);
        }

        UUID newProductId = productId != null ? productId : policy.getProductId();

        // Verify product if it changed (HD-2: product changes must still pass verification)
        if (newProductId != null && !newProductId.equals(policy.getProductId())) {
            productVerificationContract.verifyProductActive(newProductId);
        }

        // Resolve dates and sumAssured (null means keep existing)
        LocalDate newEffectiveDate = effectiveDate != null ? effectiveDate : policy.getEffectiveDate();
        LocalDate newExpiryDate = expiryDate != null ? expiryDate : policy.getExpiryDate();
        BigDecimal newSumAssured = sumAssured != null ? sumAssured : policy.getSumAssured();

        // Validate date invariant if both dates are now set
        if (newEffectiveDate != null && newExpiryDate != null && !newEffectiveDate.isBefore(newExpiryDate)) {
            throw new IllegalArgumentException("Effective date must be before expiry date");
        }

        Policy updatedPolicy = new Policy(
            policy.getPolicyId(),
            policy.getPolicyNumber(),
            policy.getCreatedBy(),
            policy.getCreatedAt(),
            newCustomerId,
            newInsurerId,
            newProductId,
            agentAId != null ? agentAId : policy.getAgentAId(),
            agentBId != null ? agentBId : policy.getAgentBId(),
            branchId != null ? branchId : policy.getBranchId(),
            policy.getPremium(),
            newEffectiveDate,
            newExpiryDate,
            newSumAssured,
            policy.getStatus(),
            policy.getVersion()
        );

        assertScope(scope, updatedPolicy);

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

        if (policy.getInsurerId() == null) {
            throw new IllegalArgumentException("Policy cannot be activated without an insurer");
        }
        insurerVerificationContract.verifyInsurerActive(policy.getInsurerId());

        // Verify product is still active at activation time (REQ-DEC-011 §7)
        if (policy.getProductId() != null) {
            productVerificationContract.verifyProductActive(policy.getProductId());
        }

        boolean authoritativeCommissionStatus = commissionManagementService.isCommissionConfigured(policyId);
        policy.activate(authoritativeCommissionStatus);

        Policy saved = policyRepositoryPort.save(policy);

        PolicyActivatedEvent event = PolicyActivatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(),
                saved.getCustomerId(), saved.getProductId(),
                saved.getAgentAId(), saved.getAgentBId(),
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium(),
                saved.getEffectiveDate(), saved.getExpiryDate(), saved.getSumAssured());
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
                saved.getCustomerId(), saved.getProductId(),
                saved.getAgentAId(), saved.getAgentBId(),
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium(),
                saved.getEffectiveDate(), saved.getExpiryDate(), saved.getSumAssured());
        eventPublisher.publishEvent(event);
    }

    @Transactional
    public void reactivatePolicy(UUID identityId, String role, UUID policyId, boolean isCommissionConfigured) {
        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        Policy policy = getScopedPolicy(policyId, scope);

        if (policy.getInsurerId() == null) {
            throw new IllegalArgumentException("Policy cannot be reactivated without an insurer");
        }
        insurerVerificationContract.verifyInsurerActive(policy.getInsurerId());

        // Verify product is still active at reactivation time
        if (policy.getProductId() != null) {
            productVerificationContract.verifyProductActive(policy.getProductId());
        }

        boolean authoritativeCommissionStatus = commissionManagementService.isCommissionConfigured(policyId);
        policy.activate(authoritativeCommissionStatus);

        Policy saved = policyRepositoryPort.save(policy);

        PolicyReactivatedEvent event = PolicyReactivatedEvent.create(
                saved.getPolicyId(), saved.getVersion(), saved.getPolicyNumber(),
                saved.getCustomerId(), saved.getProductId(),
                saved.getAgentAId(), saved.getAgentBId(),
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium(),
                saved.getEffectiveDate(), saved.getExpiryDate(), saved.getSumAssured());
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
                saved.getCustomerId(), saved.getProductId(),
                saved.getAgentAId(), saved.getAgentBId(),
                saved.getBranchId(), saved.getStatus().name(), saved.getPremium(),
                saved.getEffectiveDate(), saved.getExpiryDate(), saved.getSumAssured());

        eventPublisher.publishEvent(event);
    }

    public void configureCommission(UUID identityId, String role, UUID policyId,
                                    String commissionType, BigDecimal totalCommissionValue,
                                    BigDecimal agentAShare, BigDecimal agentBShare) {
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
