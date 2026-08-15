package com.anverraglobal.commission.application;

import com.anverraglobal.commission.application.port.outbound.CommissionRepositoryPort;
import com.anverraglobal.commission.domain.Commission;
import com.anverraglobal.commission.domain.CommissionStatus;
import com.anverraglobal.commission.domain.CommissionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommissionManagementServiceTest {

    @Mock
    private CommissionRepositoryPort commissionRepositoryPort;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    private CommissionManagementServiceImpl commissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commissionService = new CommissionManagementServiceImpl(commissionRepositoryPort, eventPublisher);
    }

    @Test
    void isCommissionConfigured_WhenMissing_ShouldReturnFalse() {
        UUID policyId = UUID.randomUUID();
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.empty());

        boolean result = commissionService.isCommissionConfigured(policyId);
        assertThat(result).isFalse();
    }

    @Test
    void isCommissionConfigured_WhenUnset_ShouldReturnFalse() {
        UUID policyId = UUID.randomUUID();
        Commission commission = Commission.createUnset(policyId);
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.of(commission));

        boolean result = commissionService.isCommissionConfigured(policyId);
        assertThat(result).isFalse();
    }

    @Test
    void isCommissionConfigured_WhenConfigured_ShouldReturnTrue() {
        UUID policyId = UUID.randomUUID();
        Commission commission = Commission.createUnset(policyId);
        commission.configure(CommissionType.FIXED, new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("50"), new BigDecimal("500"));
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.of(commission));

        boolean result = commissionService.isCommissionConfigured(policyId);
        assertThat(result).isTrue();
    }

    @Test
    void isCommissionConfigured_WhenConfiguredZero_ShouldReturnTrue() {
        UUID policyId = UUID.randomUUID();
        Commission commission = Commission.createUnset(policyId);
        commission.configure(CommissionType.FIXED, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("500"));
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.of(commission));

        boolean result = commissionService.isCommissionConfigured(policyId);
        assertThat(result).isTrue();
    }

    @Test
    void configureCommission_WhenInitiallyMissing_ShouldCreateUnsetAndConfigure() {
        UUID policyId = UUID.randomUUID();
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.empty());
        when(commissionRepositoryPort.save(any(Commission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        commissionService.configureCommission(policyId, "FIXED", new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("50"), new BigDecimal("500"));

        verify(commissionRepositoryPort).save(argThat(c -> 
                c.getPolicyId().equals(policyId) && 
                c.getStatus() == CommissionStatus.CONFIGURED && 
                c.getTotalCommissionValue().compareTo(new BigDecimal("100")) == 0
        ));
        verify(eventPublisher).publishEvent(any(Object.class));
    }

    @Test
    void resetToUnset_ShouldResetAndPublishEvent() {
        UUID policyId = UUID.randomUUID();
        Commission commission = Commission.createUnset(policyId);
        commission.configure(CommissionType.FIXED, new BigDecimal("100"), new BigDecimal("50"), new BigDecimal("50"), new BigDecimal("500"));
        
        when(commissionRepositoryPort.findById(policyId)).thenReturn(Optional.of(commission));
        when(commissionRepositoryPort.save(any(Commission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        commissionService.resetToUnset(policyId);

        verify(commissionRepositoryPort).save(argThat(c -> c.getStatus() == CommissionStatus.UNSET));
        verify(eventPublisher).publishEvent(any(Object.class));
    }
}
