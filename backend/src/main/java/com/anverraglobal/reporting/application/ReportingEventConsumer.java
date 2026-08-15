package com.anverraglobal.reporting.application;

import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class ReportingEventConsumer {

    private final ReportingApplicationService reportingApplicationService;

    public ReportingEventConsumer(ReportingApplicationService reportingApplicationService) {
        this.reportingApplicationService = reportingApplicationService;
    }

    @ApplicationModuleListener
    public void on(PolicyCreatedEvent event) {
        reportingApplicationService.processPolicyCreatedEvent(event);
    }

    @ApplicationModuleListener
    public void on(PolicyActivatedEvent event) {
        reportingApplicationService.processPolicyActivatedEvent(event);
    }

    @ApplicationModuleListener
    public void on(PolicyDeactivatedEvent event) {
        reportingApplicationService.processPolicyDeactivatedEvent(event);
    }

    @ApplicationModuleListener
    public void on(PolicyReactivatedEvent event) {
        reportingApplicationService.processPolicyReactivatedEvent(event);
    }

    @ApplicationModuleListener
    public void on(PolicyPremiumUpdatedEvent event) {
        reportingApplicationService.processPolicyPremiumUpdatedEvent(event);
    }

    @ApplicationModuleListener
    public void on(CommissionConfiguredEvent event) {
        reportingApplicationService.processCommissionConfiguredEvent(event);
    }
}
