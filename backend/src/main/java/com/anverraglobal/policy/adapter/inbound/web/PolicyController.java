package com.anverraglobal.policy.adapter.inbound.web;

import com.anverraglobal.policy.application.PolicyManagementApplicationService;
import com.anverraglobal.policy.domain.Policy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PolicyManagementApplicationService policyService;

    public PolicyController(PolicyManagementApplicationService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            Principal principal,
            @RequestBody CreatePolicyRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        Policy policy = policyService.createPolicy(identityId, role, request.policyNumber(), request.customerId(), request.insurerId(), request.agentAId(), request.agentBId(), request.branchId());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(policy));
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<PolicyResponse> getPolicy(
            Principal principal,
            @PathVariable UUID policyId) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        Policy policy = policyService.getPolicy(identityId, role, policyId);
        return ResponseEntity.ok(mapToResponse(policy));
    }

    @GetMapping
    public ResponseEntity<Page<PolicyResponse>> listPolicies(
            Principal principal,
            Pageable pageable) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        Page<Policy> policies = policyService.listPolicies(identityId, role, pageable);
        return ResponseEntity.ok(policies.map(this::mapToResponse));
    }

    @PatchMapping("/{policyId}")
    public ResponseEntity<PolicyResponse> updatePolicy(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody UpdatePolicyRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        Policy policy = policyService.updatePolicy(identityId, role, policyId, request.customerId(), request.insurerId(), request.agentAId(), request.agentBId(), request.branchId());
        return ResponseEntity.ok(mapToResponse(policy));
    }

    @PatchMapping("/{policyId}/premium")
    public ResponseEntity<Void> updatePremium(
            @PathVariable UUID policyId,
            @RequestBody UpdatePremiumRequest request) {
        // This invokes the atomic transaction
        policyService.updatePremium(policyId, request.premium());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resolve")
    public ResponseEntity<Map<String, UUID>> resolvePolicy(
            Principal principal,
            @RequestBody ResolvePolicyRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        UUID policyId = policyService.resolvePolicy(identityId, role, request.policyNumber());
        return ResponseEntity.ok(Map.of("policyId", policyId));
    }

    @PostMapping("/{policyId}/lifecycle/activate")
    public ResponseEntity<Void> activatePolicy(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody(required = false) LifecycleRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        boolean isCommissionConfigured = request != null && request.isCommissionConfigured();
        // The isCommissionConfigured flag from the client is kept for API compatibility
        // but is ignored by the backend which uses the authoritative Commission state.
        policyService.activatePolicy(identityId, role, policyId, isCommissionConfigured);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{policyId}/lifecycle/deactivate")
    public ResponseEntity<Void> deactivatePolicy(
            Principal principal,
            @PathVariable UUID policyId) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        policyService.deactivatePolicy(identityId, role, policyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{policyId}/lifecycle/reactivate")
    public ResponseEntity<Void> reactivatePolicy(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody(required = false) LifecycleRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        boolean isCommissionConfigured = request != null && request.isCommissionConfigured();
        // The isCommissionConfigured flag from the client is kept for API compatibility
        // but is ignored by the backend which uses the authoritative Commission state.
        policyService.reactivatePolicy(identityId, role, policyId, isCommissionConfigured);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{policyId}/commission")
    public ResponseEntity<Void> configureCommission(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody ConfigureCommissionRequest request) {
        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);
        
        policyService.configureCommission(
                identityId, 
                role, 
                policyId, 
                request.commissionType(), 
                request.totalCommissionValue(), 
                request.agentAShare(), 
                request.agentBShare()
        );
        return ResponseEntity.ok().build();
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

    private PolicyResponse mapToResponse(Policy policy) {
        return new PolicyResponse(
                policy.getPolicyId(),
                policy.getPolicyNumber(),
                policy.getCustomerId(),
                policy.getInsurerId(),
                policy.getAgentAId(),
                policy.getAgentBId(),
                policy.getBranchId(),
                policy.getPremium(),
                policy.getStatus().name()
        );
    }

    public record CreatePolicyRequest(String policyNumber, UUID customerId, UUID insurerId, UUID agentAId, UUID agentBId, UUID branchId) {}
    public record UpdatePolicyRequest(UUID customerId, UUID insurerId, UUID agentAId, UUID agentBId, UUID branchId) {}
    public record UpdatePremiumRequest(BigDecimal premium) {}
    public record ResolvePolicyRequest(String policyNumber) {}
    public record LifecycleRequest(boolean isCommissionConfigured) {}
    public record ConfigureCommissionRequest(String commissionType, BigDecimal totalCommissionValue, BigDecimal agentAShare, BigDecimal agentBShare) {}
    public record PolicyResponse(UUID policyId, String policyNumber, UUID customerId, UUID insurerId, UUID agentAId, UUID agentBId, UUID branchId, BigDecimal premium, String status) {}
}
