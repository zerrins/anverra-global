package com.anverraglobal.policy.application;

import com.anverraglobal.commission.contracts.CommissionManagementService;
import com.anverraglobal.customer.contracts.CustomerVerificationContract;
import com.anverraglobal.insurer.contracts.InsurerVerificationContract;
import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.policy.application.port.outbound.PolicyRepositoryPort;
import com.anverraglobal.policy.domain.Policy;
import com.anverraglobal.policy.domain.PolicyStatus;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PolicyManagementApplicationServiceTest {

    @Mock
    private CommissionManagementService commissionManagementService;

    @Mock
    private PolicyRepositoryPort policyRepositoryPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private OrganizationScopeResolutionService scopeResolutionService;

    @Mock
    private CustomerVerificationContract customerVerificationContract;

    @Mock
    private InsurerVerificationContract insurerVerificationContract;

    private PolicyManagementApplicationService policyService;

    private UUID identityId = UUID.randomUUID();
    private String role = "ROLE_USER";
    private OrganizationScope globalScope = new OrganizationScope(identityId, null, null, null, true, false);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policyService = new PolicyManagementApplicationService(
                commissionManagementService, policyRepositoryPort, eventPublisher, scopeResolutionService, customerVerificationContract, insurerVerificationContract);
        
        when(scopeResolutionService.resolveScope(identityId, role)).thenReturn(globalScope);
        
        // Mock save to simply return the passed policy
        when(policyRepositoryPort.save(any(Policy.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void createPolicy_ShouldPublishPolicyCreatedEvent_WhenCustomerIsVerified() {
        String policyNumber = "POL-123";
        UUID customerId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        UUID agentAId = UUID.randomUUID();

        // Simulate successful verification
        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);

        policyService.createPolicy(identityId, role, policyNumber, customerId, insurerId, agentAId, null, null);

        verify(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        verify(insurerVerificationContract).verifyInsurerActive(insurerId);
        verify(policyRepositoryPort).save(any(Policy.class));
        
        ArgumentCaptor<PolicyCreatedEvent> captor = ArgumentCaptor.forClass(PolicyCreatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        
        PolicyCreatedEvent event = captor.getValue();
        assertThat(event.policyNumber()).isEqualTo(policyNumber);
        assertThat(event.customerId()).isEqualTo(customerId);
        assertThat(event.agentAId()).isEqualTo(agentAId);
        assertThat(event.policyStatus()).isEqualTo("DRAFT");
    }

    @Test
    void createPolicy_ShouldThrowException_WhenCustomerIdIsNull() {
        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", null, null, UUID.randomUUID(), null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("customerId is required for new Policy");
    }

    @Test
    void createPolicy_ShouldThrowException_WhenCustomerVerificationFails() {
        UUID customerId = UUID.randomUUID();
        
        doThrow(new IllegalStateException("Customer is not ACTIVE"))
                .when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);

        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", customerId, null, UUID.randomUUID(), null, null))
                .isInstanceOf(IllegalStateException.class);
                
        verify(policyRepositoryPort, never()).save(any());
    }

    @Test
    void updatePolicy_ShouldVerifyCustomer_WhenCustomerIdChanges() {
        UUID policyId = UUID.randomUUID();
        UUID oldCustomerId = UUID.randomUUID();
        UUID newCustomerId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(), oldCustomerId, insurerId, null, null, null, java.math.BigDecimal.ZERO, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(newCustomerId, globalScope);
        
        policyService.updatePolicy(identityId, role, policyId, newCustomerId, null, null, null, null);
        
        verify(customerVerificationContract).verifyCustomerActiveAndInScope(newCustomerId, globalScope);
        verify(policyRepositoryPort).save(any(Policy.class));
    }

    @Test
    void updatePolicy_ShouldNotVerifyCustomer_WhenCustomerIdIsUnchanged() {
        UUID policyId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(), customerId, null, null, null, null, java.math.BigDecimal.ZERO, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.updatePolicy(identityId, role, policyId, customerId, null, null, null, null);
        
        verify(customerVerificationContract, never()).verifyCustomerActiveAndInScope(any(), any());
        verify(policyRepositoryPort).save(any(Policy.class));
    }

    @Test
    void updatePolicy_ShouldSupportLegacyNullCustomer_WhenCustomerIdPassedIsNull() {
        UUID policyId = UUID.randomUUID();
        // Legacy policy with null customer
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(), null, null, UUID.randomUUID(), null, null, java.math.BigDecimal.ZERO, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        // Pass null customerId to keep it null
        policyService.updatePolicy(identityId, role, policyId, null, null, null, null, null);
        
        verify(customerVerificationContract, never()).verifyCustomerActiveAndInScope(any(), any());
        
        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);
        verify(policyRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getCustomerId()).isNull();
    }

    @Test
    void activatePolicy_WhenAgentPresentAndCommissionUnset_ShouldThrowException() {
        UUID policyId = UUID.randomUUID();
        Policy policy = Policy.createDraft("POL-123", identityId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        // Backend says UNSET
        when(commissionManagementService.isCommissionConfigured(policyId)).thenReturn(false);

        // Even if client says true, it should fail
        assertThatThrownBy(() -> policyService.activatePolicy(identityId, role, policyId, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Activation prohibited: Policy has agents but commission is UNSET");

        verify(policyRepositoryPort, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any(PolicyActivatedEvent.class));
    }

    @Test
    void activatePolicy_WhenAgentPresentAndCommissionConfigured_ShouldSucceedAndPublishEvent() {
        UUID policyId = UUID.randomUUID();
        Policy policy = Policy.createDraft("POL-123", identityId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        // Backend says CONFIGURED
        when(commissionManagementService.isCommissionConfigured(policyId)).thenReturn(true);

        // Even if client says false, it should succeed
        policyService.activatePolicy(identityId, role, policyId, false);

        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        verify(policyRepositoryPort).save(policy);
        
        ArgumentCaptor<PolicyActivatedEvent> captor = ArgumentCaptor.forClass(PolicyActivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().policyStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void deactivatePolicy_ShouldSucceedAndPublishEvent() {
        UUID policyId = UUID.randomUUID();
        Policy policy = Policy.createDraft("POL-123", identityId, UUID.randomUUID(), UUID.randomUUID(), null, null, null);
        policy.activate(true); // 0 agents, doesn't matter
        
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.deactivatePolicy(identityId, role, policyId);

        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.INACTIVE);
        verify(policyRepositoryPort).save(policy);
        
        ArgumentCaptor<PolicyDeactivatedEvent> captor = ArgumentCaptor.forClass(PolicyDeactivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().policyStatus()).isEqualTo("INACTIVE");
    }

    @Test
    void reactivatePolicy_ShouldSucceedAndPublishEvent() {
        UUID policyId = UUID.randomUUID();
        Policy policy = Policy.createDraft("POL-123", identityId, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null);
        // Backend says CONFIGURED for activation
        when(commissionManagementService.isCommissionConfigured(policyId)).thenReturn(true);
        policy.activate(true);
        policy.deactivate();
        
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.reactivatePolicy(identityId, role, policyId, false);

        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        verify(policyRepositoryPort).save(policy);
        
        ArgumentCaptor<PolicyReactivatedEvent> captor = ArgumentCaptor.forClass(PolicyReactivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().policyStatus()).isEqualTo("ACTIVE");
    }
}
