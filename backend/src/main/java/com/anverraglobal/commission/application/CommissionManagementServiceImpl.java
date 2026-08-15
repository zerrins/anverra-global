package com.anverraglobal.commission.application;

import com.anverraglobal.commission.contracts.CommissionManagementService;
import com.anverraglobal.commission.domain.Commission;
import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.commission.application.port.outbound.CommissionRepositoryPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CommissionManagementServiceImpl implements CommissionManagementService {

    private final CommissionRepositoryPort commissionRepositoryPort;
    private final ApplicationEventPublisher eventPublisher;

    public CommissionManagementServiceImpl(CommissionRepositoryPort commissionRepositoryPort, ApplicationEventPublisher eventPublisher) {
        this.commissionRepositoryPort = commissionRepositoryPort;
        this.eventPublisher = eventPublisher;
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public void resetToUnset(UUID policyId) {
        java.util.Optional<Commission> optionalCommission = commissionRepositoryPort.findById(policyId);
        if (optionalCommission.isEmpty()) {
            return; // missing means UNSET semantically, no-op required
        }
        
        Commission commission = optionalCommission.get();
        commission.resetToUnset();
        
        Commission saved = commissionRepositoryPort.save(commission);
        
        CommissionConfiguredEvent event = CommissionConfiguredEvent.create(
                saved.getPolicyId(),
                saved.getVersion(),
                saved.getStatus().name(),
                saved.getType() != null ? saved.getType().name() : null,
                saved.getTotalCommissionValue(),
                saved.getAgentAShare(),
                saved.getAgentBShare()
        );
        
        eventPublisher.publishEvent(event);
    }

    @org.springframework.transaction.annotation.Transactional
    @Override
    public void configureCommission(UUID policyId, String commissionType, java.math.BigDecimal totalCommissionValue, java.math.BigDecimal agentAShare, java.math.BigDecimal agentBShare, java.math.BigDecimal policyPremium) {
        Commission commission = commissionRepositoryPort.findById(policyId)
                .orElseGet(() -> Commission.createUnset(policyId));
        
        com.anverraglobal.commission.domain.CommissionType typeEnum = commissionType != null 
                ? com.anverraglobal.commission.domain.CommissionType.valueOf(commissionType.toUpperCase()) 
                : null;
        
        commission.configure(typeEnum, totalCommissionValue, agentAShare, agentBShare, policyPremium);
        
        Commission saved = commissionRepositoryPort.save(commission);
        
        CommissionConfiguredEvent event = CommissionConfiguredEvent.create(
                saved.getPolicyId(),
                saved.getVersion(),
                saved.getStatus().name(),
                saved.getType() != null ? saved.getType().name() : null,
                saved.getTotalCommissionValue(),
                saved.getAgentAShare(),
                saved.getAgentBShare()
        );
        
        eventPublisher.publishEvent(event);
    }

    @Override
    public boolean isCommissionConfigured(UUID policyId) {
        return commissionRepositoryPort.findById(policyId)
                .map(c -> c.getStatus() == com.anverraglobal.commission.domain.CommissionStatus.CONFIGURED)
                .orElse(false);
    }
}
