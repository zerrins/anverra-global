package com.anverraglobal.reporting.application;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.reporting.application.dto.CommissionStatisticsResponse;
import com.anverraglobal.reporting.application.dto.PolicyStatisticsResponse;
import com.anverraglobal.reporting.application.port.outbound.ReportingOutboundPort;
import org.springframework.stereotype.Service;

@Service
public class ReportingApplicationService {

    private final ReportingOutboundPort reportingOutboundPort;

    public ReportingApplicationService(ReportingOutboundPort reportingOutboundPort) {
        this.reportingOutboundPort = reportingOutboundPort;
    }

    public PolicyStatisticsResponse getPolicyStatistics(OrganizationScope scope) {
        return reportingOutboundPort.getPolicyStatistics(scope);
    }

    public CommissionStatisticsResponse getCommissionStatistics(OrganizationScope scope) {
        return reportingOutboundPort.getCommissionStatistics(scope);
    }

    public void processPolicyCreatedEvent(com.anverraglobal.policy.event.PolicyCreatedEvent event) {
        reportingOutboundPort.savePolicyCreatedEvent(event);
    }

    public void processPolicyActivatedEvent(com.anverraglobal.policy.event.PolicyActivatedEvent event) {
        reportingOutboundPort.savePolicyActivatedEvent(event);
    }

    public void processPolicyDeactivatedEvent(com.anverraglobal.policy.event.PolicyDeactivatedEvent event) {
        reportingOutboundPort.savePolicyDeactivatedEvent(event);
    }

    public void processPolicyReactivatedEvent(com.anverraglobal.policy.event.PolicyReactivatedEvent event) {
        reportingOutboundPort.savePolicyReactivatedEvent(event);
    }

    public void processPolicyPremiumUpdatedEvent(com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent event) {
        reportingOutboundPort.savePolicyPremiumUpdatedEvent(event);
    }

    public void processCommissionConfiguredEvent(com.anverraglobal.commission.event.CommissionConfiguredEvent event) {
        reportingOutboundPort.saveCommissionConfiguredEvent(event);
    }
}
