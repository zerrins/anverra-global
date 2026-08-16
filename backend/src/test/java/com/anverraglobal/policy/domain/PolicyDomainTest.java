package com.anverraglobal.policy.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PolicyDomainTest {

    /**
     * Creates a fully-qualified DRAFT policy with all activation-ready fields supplied.
     * Tests that need partial state should use Policy.createDraft(...) directly.
     */
    private Policy createActivatablePolicy(UUID agentA, UUID agentB) {
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        UUID insurerId = UUID.randomUUID();
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), customerId, insurerId, productId, agentA, agentB, null);
        policy.updateDates(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        policy.updateSumAssured(new BigDecimal("50000.00"));
        return policy;
    }

    @Test
    void testValidLifecycleTransitions() {
        Policy policy = createActivatablePolicy(null, null);
        assertEquals(PolicyStatus.DRAFT, policy.getStatus());

        // DRAFT -> ACTIVE
        policy.activate(false);
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());

        // ACTIVE -> INACTIVE
        policy.deactivate();
        assertEquals(PolicyStatus.INACTIVE, policy.getStatus());

        // INACTIVE -> ACTIVE
        policy.activate(false);
        assertEquals(PolicyStatus.ACTIVE, policy.getStatus());
    }

    @Test
    void testInvalidLifecycleTransitions() {
        Policy policy = createActivatablePolicy(null, null);

        // Cannot deactivate DRAFT
        assertThrows(IllegalStateException.class, policy::deactivate);

        policy.activate(false);

        // Cannot activate ACTIVE
        assertThrows(IllegalStateException.class, () -> policy.activate(false));

        policy.deactivate();

        // Cannot deactivate INACTIVE
        assertThrows(IllegalStateException.class, policy::deactivate);
    }

    @Test
    void testActivation_ZeroAgents_UnsetCommission_Allowed() {
        Policy policy = createActivatablePolicy(null, null);
        assertDoesNotThrow(() -> policy.activate(false));
    }

    @Test
    void testActivation_ZeroAgents_ConfiguredCommission_Allowed() {
        Policy policy = createActivatablePolicy(null, null);
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testActivation_OneAgent_UnsetCommission_Prohibited() {
        Policy policy = createActivatablePolicy(UUID.randomUUID(), null);
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Activation prohibited"));
    }

    @Test
    void testActivation_OneAgent_ConfiguredCommission_Allowed() {
        Policy policy = createActivatablePolicy(UUID.randomUUID(), null);
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testActivation_TwoAgents_UnsetCommission_Prohibited() {
        Policy policy = createActivatablePolicy(UUID.randomUUID(), UUID.randomUUID());
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Activation prohibited"));
    }

    @Test
    void testActivation_TwoAgents_ConfiguredCommission_Allowed() {
        Policy policy = createActivatablePolicy(UUID.randomUUID(), UUID.randomUUID());
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testAgentOrderValidation() {
        assertThrows(IllegalArgumentException.class, () ->
            Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), null)
        );
    }

    @Test
    void testActivation_MissingInsurer_Prohibited() {
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), null, null, null);
        policy.updateDates(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        policy.updateSumAssured(new BigDecimal("50000.00"));
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Policy must have an insurer"));
    }

    @Test
    void testActivation_MissingProduct_Prohibited() {
        // createDraft enforces productId != null, so we test via reconstruction
        Policy policy = new Policy(UUID.randomUUID(), "POL-123", UUID.randomUUID(), java.time.Instant.now(),
                UUID.randomUUID(), UUID.randomUUID(), null, null, null, null,
                BigDecimal.ZERO, LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1),
                new BigDecimal("50000.00"), PolicyStatus.DRAFT, null);
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Policy must have a product"));
    }

    @Test
    void testActivation_MissingEffectiveDate_Prohibited() {
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null);
        // Leave effectiveDate null
        policy.updateDates(null, LocalDate.of(2026, 1, 1));
        policy.updateSumAssured(new BigDecimal("50000.00"));
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("effective date"));
    }

    @Test
    void testActivation_MissingExpiryDate_Prohibited() {
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null);
        policy.updateDates(LocalDate.of(2025, 1, 1), null);
        policy.updateSumAssured(new BigDecimal("50000.00"));
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("expiry date"));
    }

    @Test
    void testActivation_EffectiveAfterExpiry_Prohibited() {
        assertThrows(IllegalArgumentException.class, () ->
            Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null)
                    .updateDates(LocalDate.of(2026, 1, 1), LocalDate.of(2025, 1, 1))
        );
    }

    @Test
    void testActivation_MissingSumAssured_Prohibited() {
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null);
        policy.updateDates(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        // sumAssured remains null
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("sum assured"));
    }

    @Test
    void testActivation_NegativeSumAssured_Prohibited() {
        assertThrows(IllegalArgumentException.class, () ->
            Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null)
                    .updateSumAssured(new BigDecimal("-1.00"))
        );
    }

    @Test
    void testActivation_ZeroSumAssured_Allowed() {
        Policy policy = Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null);
        policy.updateDates(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 1, 1));
        policy.updateSumAssured(BigDecimal.ZERO);
        assertDoesNotThrow(() -> policy.activate(false));
    }

    @Test
    void testCreateDraft_NullProductId_Prohibited() {
        assertThrows(IllegalArgumentException.class, () ->
            Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null, null, null, null)
        );
    }

    @Test
    void testCreateDraft_NullCustomerId_Prohibited() {
        assertThrows(IllegalArgumentException.class, () ->
            Policy.createDraft("POL-123", UUID.randomUUID(), null, UUID.randomUUID(), UUID.randomUUID(), null, null, null)
        );
    }

    @Test
    void testLegacyReconstruction_NullFieldsAllowed() {
        // Legacy policies may have null productId, dates, sumAssured — reconstruction must succeed
        Policy policy = new Policy(UUID.randomUUID(), "POL-123", UUID.randomUUID(), java.time.Instant.now(),
                UUID.randomUUID(), null, null, null, null, null,
                BigDecimal.ZERO, null, null, null, PolicyStatus.DRAFT, 1L);
        assertNull(policy.getInsurerId());
        assertNull(policy.getProductId());
        assertNull(policy.getEffectiveDate());
        assertNull(policy.getExpiryDate());
        assertNull(policy.getSumAssured());
    }
}
