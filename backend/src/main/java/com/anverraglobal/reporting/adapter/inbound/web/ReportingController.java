package com.anverraglobal.reporting.adapter.inbound.web;

import com.anverraglobal.organization.contracts.OrganizationScopeResolutionService;
import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.reporting.application.ReportingApplicationService;
import com.anverraglobal.reporting.application.dto.CommissionStatisticsResponse;
import com.anverraglobal.reporting.application.dto.PolicyStatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reporting")
public class ReportingController {

    private static final String DATA_ENTRY_DENIED_MESSAGE =
            "Data Entry users are not authorized to access Reporting Statistics.";

    private final ReportingApplicationService reportingApplicationService;
    private final OrganizationScopeResolutionService scopeResolutionService;

    public ReportingController(ReportingApplicationService reportingApplicationService,
                               OrganizationScopeResolutionService scopeResolutionService) {
        this.reportingApplicationService = reportingApplicationService;
        this.scopeResolutionService = scopeResolutionService;
    }

    @GetMapping("/policies/statistics")
    public ResponseEntity<PolicyStatisticsResponse> getPolicyStatistics(Principal principal) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        enforceDataEntryRestriction(scope);

        PolicyStatisticsResponse response = reportingApplicationService.getPolicyStatistics(scope);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/commissions/statistics")
    public ResponseEntity<CommissionStatisticsResponse> getCommissionStatistics(Principal principal) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        OrganizationScope scope = scopeResolutionService.resolveScope(identityId, role);
        enforceDataEntryRestriction(scope);

        CommissionStatisticsResponse response = reportingApplicationService.getCommissionStatistics(scope);
        return ResponseEntity.ok(response);
    }

    /**
     * Categorical prohibition: Data Entry users must not access any Reporting aggregate statistics.
     * The isDataEntry flag is set authoritatively by the Organization module's scope resolution contract.
     * This check must occur before any application service is invoked.
     */
    private void enforceDataEntryRestriction(OrganizationScope scope) {
        if (scope.isDataEntry()) {
            throw new AccessDeniedException(DATA_ENTRY_DENIED_MESSAGE);
        }
    }

    private UUID extractIdentityId(Principal principal) {
        if (principal == null) return UUID.fromString("00000000-0000-0000-0000-000000000000");
        try {
            return UUID.fromString(principal.getName());
        } catch (Exception e) {
            return UUID.fromString("00000000-0000-0000-0000-000000000000");
        }
    }

    private String extractRole(Principal principal) {
        if (principal instanceof org.springframework.security.core.Authentication auth) {
            if (!auth.getAuthorities().isEmpty()) {
                return auth.getAuthorities().iterator().next().getAuthority();
            }
        }
        return "ROLE_USER"; // fallback
    }
}
