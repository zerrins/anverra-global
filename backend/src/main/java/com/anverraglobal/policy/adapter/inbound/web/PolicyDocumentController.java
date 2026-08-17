package com.anverraglobal.policy.adapter.inbound.web;

import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService;
import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService.DocumentDownloadInfo;
import com.anverraglobal.policy.application.PolicyDocumentManagementApplicationService.PresignedUploadInfo;
import com.anverraglobal.policy.domain.PolicyDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies/{policyId}/document")
public class PolicyDocumentController {

    private final PolicyDocumentManagementApplicationService documentService;

    public PolicyDocumentController(PolicyDocumentManagementApplicationService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/presigned-upload")
    public ResponseEntity<PresignedUploadResponse> generateUploadUrl(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody PresignedUploadRequest request) {

        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        PresignedUploadInfo info = documentService.generateUploadUrl(
                identityId, role, policyId, request.originalFilename(), request.contentType()
        );

        return ResponseEntity.ok(new PresignedUploadResponse(info.storageKey(), info.uploadUrl()));
    }

    @PutMapping
    public ResponseEntity<PolicyDocumentResponse> registerDocument(
            Principal principal,
            @PathVariable UUID policyId,
            @RequestBody RegisterPolicyDocumentRequest request) {

        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        PolicyDocument doc = documentService.registerDocument(
                identityId, role, policyId,
                request.storageKey(), request.originalFilename(),
                request.contentType(), request.sizeBytes()
        );

        return ResponseEntity.ok(mapToResponse(doc, null));
    }

    @GetMapping
    public ResponseEntity<PolicyDocumentResponse> getDocument(
            Principal principal,
            @PathVariable UUID policyId) {

        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        DocumentDownloadInfo info = documentService.getDocument(identityId, role, policyId);

        return ResponseEntity.ok(mapToResponse(info.document(), info.downloadUrl()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteDocument(
            Principal principal,
            @PathVariable UUID policyId) {

        UUID identityId = extractIdentityId(principal);
        String role = extractRole(principal);

        documentService.removeDocument(identityId, role, policyId);

        return ResponseEntity.noContent().build();
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

    private PolicyDocumentResponse mapToResponse(PolicyDocument doc, String downloadUrl) {
        return new PolicyDocumentResponse(
                doc.getPolicyId(),
                doc.getOriginalFilename(),
                doc.getContentType(),
                doc.getSizeBytes(),
                downloadUrl
        );
    }

    public record PresignedUploadRequest(String originalFilename, String contentType) {}
    public record PresignedUploadResponse(String storageKey, String uploadUrl) {}

    public record RegisterPolicyDocumentRequest(String storageKey, String originalFilename, String contentType, Long sizeBytes) {}
    public record PolicyDocumentResponse(UUID policyId, String originalFilename, String contentType, Long sizeBytes, String downloadUrl) {}
}
