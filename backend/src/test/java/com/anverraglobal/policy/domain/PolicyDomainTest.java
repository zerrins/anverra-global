package com.anverraglobal.policy.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PolicyDomainTest {

    private Policy createTestPolicy(UUID agentA, UUID agentB) {
        return Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), agentA, agentB, null);
    }

    @Test
    void testValidLifecycleTransitions() {
        Policy policy = createTestPolicy(null, null);
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
        Policy policy = createTestPolicy(null, null);

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
        Policy policy = createTestPolicy(null, null);
        assertDoesNotThrow(() -> policy.activate(false));
    }

    @Test
    void testActivation_ZeroAgents_ConfiguredCommission_Allowed() {
        Policy policy = createTestPolicy(null, null);
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testActivation_OneAgent_UnsetCommission_Prohibited() {
        Policy policy = createTestPolicy(UUID.randomUUID(), null);
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Activation prohibited"));
    }

    @Test
    void testActivation_OneAgent_ConfiguredCommission_Allowed() {
        Policy policy = createTestPolicy(UUID.randomUUID(), null);
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testActivation_TwoAgents_UnsetCommission_Prohibited() {
        Policy policy = createTestPolicy(UUID.randomUUID(), UUID.randomUUID());
        Exception ex = assertThrows(IllegalStateException.class, () -> policy.activate(false));
        assertTrue(ex.getMessage().contains("Activation prohibited"));
    }

    @Test
    void testActivation_TwoAgents_ConfiguredCommission_Allowed() {
        Policy policy = createTestPolicy(UUID.randomUUID(), UUID.randomUUID());
        assertDoesNotThrow(() -> policy.activate(true));
    }

    @Test
    void testAgentOrderValidation() {
        assertThrows(IllegalArgumentException.class, () -> 
            Policy.createDraft("POL-123", UUID.randomUUID(), UUID.randomUUID(), null, UUID.randomUUID(), null)
        );
    }
}
