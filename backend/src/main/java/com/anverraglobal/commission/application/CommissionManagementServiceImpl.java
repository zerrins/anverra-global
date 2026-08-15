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

    @Override
    public void resetToUnset(UUID policyId) {
        Commission commission = commissionRepositoryPort.findById(policyId)
                .orElseThrow(() -> new IllegalArgumentException("Commission not found for policy: " + policyId));
        
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
                .orElseThrow(() -> new java.util.NoSuchElementException("Commission not found for policy: " + policyId));
        
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
}
