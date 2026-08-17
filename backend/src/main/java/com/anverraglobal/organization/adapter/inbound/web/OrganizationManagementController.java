package com.anverraglobal.organization.adapter.inbound.web;

import com.anverraglobal.organization.application.OrganizationManagementApplicationService;
import com.anverraglobal.organization.domain.Branch;
import com.anverraglobal.organization.domain.Dealer;
import com.anverraglobal.organization.domain.OrganizationStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrganizationManagementController {

    private final OrganizationManagementApplicationService organizationService;

    public OrganizationManagementController(OrganizationManagementApplicationService organizationService) {
        this.organizationService = organizationService;
    }

    // Dealer Endpoints

    @PostMapping("/dealers")
    public ResponseEntity<DealerResponse> createDealer(
            @RequestBody DealerRequest request,
            Authentication authentication) {
        Dealer dealer = organizationService.createDealer(request.name(), getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToDealerResponse(dealer));
    }

    @GetMapping("/dealers")
    public ResponseEntity<List<DealerResponse>> listDealers(Authentication authentication) {
        List<DealerResponse> dealers = organizationService.listDealers(getIdentityId(authentication), getPrimaryRole(authentication))
                .stream().map(this::mapToDealerResponse).toList();
        return ResponseEntity.ok(dealers);
    }

    @GetMapping("/dealers/{id}")
    public ResponseEntity<DealerResponse> getDealer(
            @PathVariable UUID id,
            Authentication authentication) {
        Dealer dealer = organizationService.getDealer(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToDealerResponse(dealer));
    }

    @PutMapping("/dealers/{id}")
    public ResponseEntity<DealerResponse> updateDealer(
            @PathVariable UUID id,
            @RequestBody DealerRequest request,
            Authentication authentication) {
        Dealer dealer = organizationService.updateDealer(id, request.name(), getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToDealerResponse(dealer));
    }

    @PostMapping("/dealers/{id}/lifecycle/activate")
    public ResponseEntity<DealerResponse> activateDealer(
            @PathVariable UUID id,
            Authentication authentication) {
        Dealer dealer = organizationService.activateDealer(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToDealerResponse(dealer));
    }

    @PostMapping("/dealers/{id}/lifecycle/deactivate")
    public ResponseEntity<DealerResponse> deactivateDealer(
            @PathVariable UUID id,
            Authentication authentication) {
        Dealer dealer = organizationService.deactivateDealer(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToDealerResponse(dealer));
    }

    // Branch Endpoints

    @PostMapping("/branches")
    public ResponseEntity<BranchResponse> createBranch(
            @RequestBody BranchRequest request,
            Authentication authentication) {
        Branch branch = organizationService.createBranch(request.dealerId(), request.name(), getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToBranchResponse(branch));
    }

    @GetMapping("/branches")
    public ResponseEntity<List<BranchResponse>> listBranches(
            @RequestParam UUID dealerId,
            Authentication authentication) {
        List<BranchResponse> branches = organizationService.listBranches(dealerId, getIdentityId(authentication), getPrimaryRole(authentication))
                .stream().map(this::mapToBranchResponse).toList();
        return ResponseEntity.ok(branches);
    }

    @GetMapping("/branches/{id}")
    public ResponseEntity<BranchResponse> getBranch(
            @PathVariable UUID id,
            Authentication authentication) {
        Branch branch = organizationService.getBranch(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToBranchResponse(branch));
    }

    @PutMapping("/branches/{id}")
    public ResponseEntity<BranchResponse> updateBranch(
            @PathVariable UUID id,
            @RequestBody BranchUpdateRequest request,
            Authentication authentication) {
        Branch branch = organizationService.updateBranch(id, request.name(), getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToBranchResponse(branch));
    }

    @PostMapping("/branches/{id}/lifecycle/activate")
    public ResponseEntity<BranchResponse> activateBranch(
            @PathVariable UUID id,
            Authentication authentication) {
        Branch branch = organizationService.activateBranch(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToBranchResponse(branch));
    }

    @PostMapping("/branches/{id}/lifecycle/deactivate")
    public ResponseEntity<BranchResponse> deactivateBranch(
            @PathVariable UUID id,
            Authentication authentication) {
        Branch branch = organizationService.deactivateBranch(id, getIdentityId(authentication), getPrimaryRole(authentication));
        return ResponseEntity.ok(mapToBranchResponse(branch));
    }

    // DTOs

    public record DealerRequest(String name) {}
    public record BranchRequest(UUID dealerId, String name) {}
    public record BranchUpdateRequest(String name) {}
    public record DealerResponse(UUID id, String name, OrganizationStatus status) {}
    public record BranchResponse(UUID id, UUID dealerId, String name, OrganizationStatus status) {}

    // Security Helpers

    private UUID getIdentityId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken jwtToken) {
            String id = jwtToken.getToken().getClaimAsString("https://anverraglobal.com/identity_id");
            if (id != null) {
                return UUID.fromString(id);
            }
        }
        return UUID.fromString(authentication.getName());
    }

    private String getPrimaryRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .findFirst()
                .orElse("ROLE_USER");
    }

    private DealerResponse mapToDealerResponse(Dealer dealer) {
        return new DealerResponse(dealer.getId(), dealer.getName(), dealer.getStatus());
    }

    private BranchResponse mapToBranchResponse(Branch branch) {
        return new BranchResponse(branch.getId(), branch.getDealerId(), branch.getName(), branch.getStatus());
    }
}
