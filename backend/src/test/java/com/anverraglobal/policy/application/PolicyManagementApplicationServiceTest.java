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
import com.anverraglobal.product.contracts.ProductVerificationContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Mock
    private ProductVerificationContract productVerificationContract;

    private PolicyManagementApplicationService policyService;

    private UUID identityId = UUID.randomUUID();
    private String role = "ROLE_USER";
    private OrganizationScope globalScope = new OrganizationScope(identityId, null, null, null, true, false);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        policyService = new PolicyManagementApplicationService(
                commissionManagementService, policyRepositoryPort, eventPublisher, scopeResolutionService,
                customerVerificationContract, insurerVerificationContract, productVerificationContract);
        
        when(scopeResolutionService.resolveScope(identityId, role)).thenReturn(globalScope);
        
        // Mock save to simply return the passed policy
        when(policyRepositoryPort.save(any(Policy.class))).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void createPolicy_ShouldPublishPolicyCreatedEvent_WhenAllVerificationsPasses() {
        String policyNumber = "POL-123";
        UUID customerId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID agentAId = UUID.randomUUID();

        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);
        doNothing().when(productVerificationContract).verifyProductActive(productId);

        policyService.createPolicy(identityId, role, policyNumber, customerId, insurerId, productId, agentAId, null, null);

        verify(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        verify(insurerVerificationContract).verifyInsurerActive(insurerId);
        verify(productVerificationContract).verifyProductActive(productId);
        verify(policyRepositoryPort).save(any(Policy.class));
        
        ArgumentCaptor<PolicyCreatedEvent> captor = ArgumentCaptor.forClass(PolicyCreatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        
        PolicyCreatedEvent event = captor.getValue();
        assertThat(event.policyNumber()).isEqualTo(policyNumber);
        assertThat(event.customerId()).isEqualTo(customerId);
        assertThat(event.productId()).isEqualTo(productId);
        assertThat(event.agentAId()).isEqualTo(agentAId);
        assertThat(event.policyStatus()).isEqualTo("DRAFT");
    }

    @Test
    void createPolicy_ShouldThrowException_WhenCustomerIdIsNull() {
        UUID productId = UUID.randomUUID();
        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", null, null, productId, UUID.randomUUID(), null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("customerId is required");
    }

    @Test
    void createPolicy_ShouldThrowException_WhenProductIdIsNull() {
        UUID customerId = UUID.randomUUID();
        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", customerId, null, null, null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("productId is required");
    }

    @Test
    void createPolicy_ShouldThrowException_WhenCustomerVerificationFails() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        doThrow(new IllegalStateException("Customer is not ACTIVE"))
                .when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);

        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", customerId, null, productId, UUID.randomUUID(), null, null))
                .isInstanceOf(IllegalStateException.class);
                
        verify(policyRepositoryPort, never()).save(any());
    }

    @Test
    void createPolicy_ShouldThrowException_WhenProductVerificationFails() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(customerId, globalScope);
        doThrow(new IllegalStateException("Product is not ACTIVE"))
                .when(productVerificationContract).verifyProductActive(productId);

        assertThatThrownBy(() -> policyService.createPolicy(identityId, role, "POL-123", customerId, null, productId, null, null, null))
                .isInstanceOf(IllegalStateException.class);
                
        verify(policyRepositoryPort, never()).save(any());
    }

    @Test
    void updatePolicy_ShouldVerifyCustomer_WhenCustomerIdChanges() {
        UUID policyId = UUID.randomUUID();
        UUID oldCustomerId = UUID.randomUUID();
        UUID newCustomerId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                oldCustomerId, insurerId, productId, null, null, null,
                java.math.BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        doNothing().when(customerVerificationContract).verifyCustomerActiveAndInScope(newCustomerId, globalScope);
        
        policyService.updatePolicy(identityId, role, policyId, newCustomerId, null, null, null, null, null, null, null, null);
        
        verify(customerVerificationContract).verifyCustomerActiveAndInScope(newCustomerId, globalScope);
        verify(policyRepositoryPort).save(any(Policy.class));
    }

    @Test
    void updatePolicy_ShouldNotVerifyCustomer_WhenCustomerIdIsUnchanged() {
        UUID policyId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                customerId, null, productId, null, null, null,
                java.math.BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.updatePolicy(identityId, role, policyId, customerId, null, null, null, null, null, null, null, null);
        
        verify(customerVerificationContract, never()).verifyCustomerActiveAndInScope(any(), any());
        verify(policyRepositoryPort).save(any(Policy.class));
    }

    @Test
    void updatePolicy_ShouldVerifyProduct_WhenProductIdChanges() {
        UUID policyId = UUID.randomUUID();
        UUID oldProductId = UUID.randomUUID();
        UUID newProductId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                customerId, null, oldProductId, null, null, null,
                BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        doNothing().when(productVerificationContract).verifyProductActive(newProductId);
        
        policyService.updatePolicy(identityId, role, policyId, null, null, newProductId, null, null, null, null, null, null);
        
        verify(productVerificationContract).verifyProductActive(newProductId);
        verify(policyRepositoryPort).save(any(Policy.class));
    }

    @Test
    void updatePolicy_ShouldUpdateDates_WhenEffectiveAndExpiryPassed() {
        UUID policyId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        LocalDate effectiveDate = LocalDate.of(2025, 6, 1);
        LocalDate expiryDate = LocalDate.of(2026, 6, 1);
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                customerId, null, productId, null, null, null,
                BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.updatePolicy(identityId, role, policyId, null, null, null, null, null, null, effectiveDate, expiryDate, null);
        
        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);
        verify(policyRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getEffectiveDate()).isEqualTo(effectiveDate);
        assertThat(captor.getValue().getExpiryDate()).isEqualTo(expiryDate);
    }

    @Test
    void updatePolicy_ShouldRejectInvalidDateRange() {
        UUID policyId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                customerId, null, productId, null, null, null,
                BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        // effectiveDate >= expiryDate
        assertThatThrownBy(() -> policyService.updatePolicy(identityId, role, policyId, null, null, null, null, null, null,
                LocalDate.of(2026, 1, 1), LocalDate.of(2025, 1, 1), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Effective date must be before expiry date");
    }

    @Test
    void updatePolicy_ShouldSupportLegacyNullCustomer_WhenCustomerIdPassedIsNull() {
        UUID policyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        // Legacy policy with null customer
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                null, null, productId, UUID.randomUUID(), null, null,
                java.math.BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        // Pass null customerId to keep it null
        policyService.updatePolicy(identityId, role, policyId, null, null, null, null, null, null, null, null, null);
        
        verify(customerVerificationContract, never()).verifyCustomerActiveAndInScope(any(), any());
        
        ArgumentCaptor<Policy> captor = ArgumentCaptor.forClass(Policy.class);
        verify(policyRepositoryPort).save(captor.capture());
        assertThat(captor.getValue().getCustomerId()).isNull();
    }

    @Test
    void activatePolicy_WhenProductVerificationFails_ShouldThrowException() {
        UUID policyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                UUID.randomUUID(), insurerId, productId, null, null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);
        doThrow(new IllegalStateException("Product is not ACTIVE"))
                .when(productVerificationContract).verifyProductActive(productId);

        assertThatThrownBy(() -> policyService.activatePolicy(identityId, role, policyId, true))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product is not ACTIVE");

        verify(policyRepositoryPort, never()).save(any());
    }

    @Test
    void activatePolicy_WhenAgentPresentAndCommissionUnset_ShouldThrowException() {
        UUID policyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                UUID.randomUUID(), insurerId, productId, UUID.randomUUID(), null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);
        doNothing().when(productVerificationContract).verifyProductActive(productId);
        
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
        UUID productId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                UUID.randomUUID(), insurerId, productId, UUID.randomUUID(), null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.DRAFT, 0L);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);
        doNothing().when(productVerificationContract).verifyProductActive(productId);
        
        // Backend says CONFIGURED
        when(commissionManagementService.isCommissionConfigured(policyId)).thenReturn(true);

        policyService.activatePolicy(identityId, role, policyId, false);

        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        verify(policyRepositoryPort).save(policy);
        
        ArgumentCaptor<PolicyActivatedEvent> captor = ArgumentCaptor.forClass(PolicyActivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().policyStatus()).isEqualTo("ACTIVE");
        assertThat(captor.getValue().productId()).isEqualTo(productId);
    }

    @Test
    void deactivatePolicy_ShouldSucceedAndPublishEvent() {
        UUID policyId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                UUID.randomUUID(), insurerId, productId, null, null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.ACTIVE, 1L);
        
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
        UUID productId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = new Policy(policyId, "POL-123", identityId, java.time.Instant.now(),
                UUID.randomUUID(), insurerId, productId, UUID.randomUUID(), null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.INACTIVE, 2L);
        
        doNothing().when(insurerVerificationContract).verifyInsurerActive(insurerId);
        doNothing().when(productVerificationContract).verifyProductActive(productId);
        when(commissionManagementService.isCommissionConfigured(policyId)).thenReturn(true);
        when(policyRepositoryPort.findByIdAndScope(policyId, globalScope)).thenReturn(Optional.of(policy));
        
        policyService.reactivatePolicy(identityId, role, policyId, false);

        assertThat(policy.getStatus()).isEqualTo(PolicyStatus.ACTIVE);
        verify(policyRepositoryPort).save(policy);
        
        ArgumentCaptor<PolicyReactivatedEvent> captor = ArgumentCaptor.forClass(PolicyReactivatedEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().policyStatus()).isEqualTo("ACTIVE");
        assertThat(captor.getValue().productId()).isEqualTo(productId);
    }
}
