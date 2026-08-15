package com.anverraglobal.reporting.application.port.outbound;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.reporting.application.dto.CommissionStatisticsResponse;
import com.anverraglobal.reporting.application.dto.PolicyStatisticsResponse;
import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;

public interface ReportingOutboundPort {
    PolicyStatisticsResponse getPolicyStatistics(OrganizationScope scope);
    CommissionStatisticsResponse getCommissionStatistics(OrganizationScope scope);

    void savePolicyCreatedEvent(PolicyCreatedEvent event);
    void savePolicyActivatedEvent(PolicyActivatedEvent event);
    void savePolicyDeactivatedEvent(PolicyDeactivatedEvent event);
    void savePolicyReactivatedEvent(PolicyReactivatedEvent event);
    void savePolicyPremiumUpdatedEvent(PolicyPremiumUpdatedEvent event);
    
    void saveCommissionConfiguredEvent(CommissionConfiguredEvent event);
}
