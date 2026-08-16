package com.anverraglobal.organization.adapter.inbound.web;

import com.anverraglobal.organization.application.OrganizationHierarchyServiceImpl;
import com.anverraglobal.organization.contracts.dto.HierarchyNodeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hierarchy")
public class OrganizationHierarchyController {

    private final OrganizationHierarchyServiceImpl hierarchyService;

    public OrganizationHierarchyController(OrganizationHierarchyServiceImpl hierarchyService) {
        this.hierarchyService = hierarchyService;
    }

    @GetMapping("/dealers")
    public ResponseEntity<List<HierarchyNodeResponse>> getDealers(Authentication authentication) {
        return ResponseEntity.ok(hierarchyService.getDealers(getIdentityId(authentication), getPrimaryRole(authentication)));
    }

    @GetMapping("/dealers/{dealerId}/branches")
    public ResponseEntity<List<HierarchyNodeResponse>> getBranches(
            @PathVariable UUID dealerId,
            Authentication authentication) {
        return ResponseEntity.ok(hierarchyService.getBranches(getIdentityId(authentication), getPrimaryRole(authentication), dealerId));
    }

    @GetMapping("/branches/{branchId}/agents")
    public ResponseEntity<List<HierarchyNodeResponse>> getAgents(
            @PathVariable UUID branchId,
            Authentication authentication) {
        return ResponseEntity.ok(hierarchyService.getAgents(getIdentityId(authentication), getPrimaryRole(authentication), branchId));
    }

    private UUID getIdentityId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String id = jwtToken.getToken().getClaimAsString("https://anverraglobal.com/identity_id");
            if (id != null) {
                return UUID.fromString(id);
            }
        }
        // Fallback for tests or other authentication types
        return UUID.fromString(authentication.getName());
    }

    private String getPrimaryRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse("ROLE_USER");
    }
}
