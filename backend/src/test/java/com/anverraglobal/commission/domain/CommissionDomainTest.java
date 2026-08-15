package com.anverraglobal.commission.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommissionDomainTest {

    @Test
    void testCreateUnset() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        assertEquals(CommissionStatus.UNSET, commission.getStatus());
        assertNull(commission.getType());
        assertNull(commission.getTotalCommissionValue());
        assertNull(commission.getAgentAShare());
        assertNull(commission.getAgentBShare());
    }

    @Test
    void testConfigure_ValidPercentage() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        BigDecimal premium = new BigDecimal("1000.00");
        BigDecimal totalVal = new BigDecimal("100.00");
        BigDecimal shareA = new BigDecimal("100.00");
        BigDecimal shareB = BigDecimal.ZERO;

        commission.configure(CommissionType.PERCENTAGE, totalVal, shareA, shareB, premium);

        assertEquals(CommissionStatus.CONFIGURED, commission.getStatus());
        assertEquals(CommissionType.PERCENTAGE, commission.getType());
        assertEquals(new BigDecimal("100.00"), commission.getTotalCommissionValue());
    }

    @Test
    void testConfigure_ZeroCommission_Allowed() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        BigDecimal premium = new BigDecimal("1000.00");

        // ZERO commission
        commission.configure(CommissionType.FIXED, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, premium);

        assertEquals(CommissionStatus.CONFIGURED, commission.getStatus());
        assertEquals(BigDecimal.ZERO, commission.getTotalCommissionValue());
    }

    @Test
    void testConfigure_ExceedsFiftyPercent_ThrowsException() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        BigDecimal premium = new BigDecimal("1000.00");
        
        // 501 is > 50% of 1000
        BigDecimal totalVal = new BigDecimal("501.00");
        BigDecimal shareA = new BigDecimal("501.00");
        BigDecimal shareB = BigDecimal.ZERO;

        Exception ex = assertThrows(IllegalStateException.class, () -> 
            commission.configure(CommissionType.PERCENTAGE, totalVal, shareA, shareB, premium)
        );
        assertTrue(ex.getMessage().contains("exceed 50%"));
    }

    @Test
    void testConfigure_SharesDoNotSum_ThrowsException() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        BigDecimal premium = new BigDecimal("1000.00");
        
        BigDecimal totalVal = new BigDecimal("100.00");
        BigDecimal shareA = new BigDecimal("60.00");
        BigDecimal shareB = new BigDecimal("30.00"); // sum is 90

        Exception ex = assertThrows(IllegalStateException.class, () -> 
            commission.configure(CommissionType.PERCENTAGE, totalVal, shareA, shareB, premium)
        );
        assertTrue(ex.getMessage().contains("Agent shares must sum exactly"));
    }

    @Test
    void testResetToUnset() {
        Commission commission = Commission.createUnset(UUID.randomUUID());
        commission.configure(CommissionType.FIXED, new BigDecimal("100.00"), new BigDecimal("100.00"), BigDecimal.ZERO, new BigDecimal("1000.00"));
        
        assertEquals(CommissionStatus.CONFIGURED, commission.getStatus());

        commission.resetToUnset();

        assertEquals(CommissionStatus.UNSET, commission.getStatus());
        assertNull(commission.getType());
        assertNull(commission.getTotalCommissionValue());
        assertNull(commission.getAgentAShare());
        assertNull(commission.getAgentBShare());
    }
}
