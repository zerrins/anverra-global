package com.anverraglobal.policy.application;

import com.anverraglobal.policy.application.port.outbound.PolicyDocumentRepositoryPort;
import com.anverraglobal.policy.domain.Policy;
import com.anverraglobal.policy.domain.PolicyDocument;
import com.anverraglobal.policy.port.outbound.DocumentStoragePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyDocumentManagementApplicationService {

    private final PolicyManagementApplicationService policyService;
    private final PolicyDocumentRepositoryPort documentRepositoryPort;
    private final DocumentStoragePort documentStoragePort;

    public PolicyDocumentManagementApplicationService(
            PolicyManagementApplicationService policyService,
            PolicyDocumentRepositoryPort documentRepositoryPort,
            DocumentStoragePort documentStoragePort) {
        this.policyService = policyService;
        this.documentRepositoryPort = documentRepositoryPort;
        this.documentStoragePort = documentStoragePort;
    }

    public PresignedUploadInfo generateUploadUrl(UUID identityId, String role, UUID policyId, String originalFilename, String contentType) {
        verifyPolicyAccess(identityId, role, policyId);

        String storageKey = generateStorageKey(policyId, originalFilename);
        String uploadUrl = documentStoragePort.generateUploadUrl(storageKey, contentType);

        return new PresignedUploadInfo(storageKey, uploadUrl);
    }

    @Transactional
    public PolicyDocument registerDocument(UUID identityId, String role, UUID policyId, String storageKey, String originalFilename, String contentType, Long sizeBytes) {
        verifyPolicyAccess(identityId, role, policyId);

        if (!storageKey.startsWith("policies/" + policyId + "/")) {
            throw new IllegalArgumentException("Invalid storage key for this policy");
        }

        Optional<PolicyDocument> existingDocOpt = documentRepositoryPort.findByPolicyId(policyId);

        PolicyDocument newDoc = PolicyDocument.createNew(policyId, storageKey, originalFilename, contentType, sizeBytes);
        PolicyDocument savedDoc = documentRepositoryPort.save(newDoc);

        if (existingDocOpt.isPresent()) {
            PolicyDocument oldDoc = existingDocOpt.get();
            if (!oldDoc.getStorageKey().equals(storageKey)) {
                try {
                    documentStoragePort.removeDocument(oldDoc.getStorageKey());
                } catch (Exception e) {
                    // Log the failure to delete old document, but do not fail the transaction.
                    // A background reconciliation process can clean up orphaned objects if needed.
                }
            }
        }

        return savedDoc;
    }

    @Transactional(readOnly = true)
    public DocumentDownloadInfo getDocument(UUID identityId, String role, UUID policyId) {
        verifyPolicyAccess(identityId, role, policyId);

        PolicyDocument doc = documentRepositoryPort.findByPolicyId(policyId)
                .orElseThrow(() -> new NoSuchElementException("Document not found for policy: " + policyId));

        String downloadUrl = documentStoragePort.generateDownloadUrl(doc.getStorageKey());

        return new DocumentDownloadInfo(doc, downloadUrl);
    }

    @Transactional
    public void removeDocument(UUID identityId, String role, UUID policyId) {
        verifyPolicyAccess(identityId, role, policyId);

        PolicyDocument doc = documentRepositoryPort.findByPolicyId(policyId)
                .orElseThrow(() -> new NoSuchElementException("Document not found for policy: " + policyId));

        documentStoragePort.removeDocument(doc.getStorageKey());
        documentRepositoryPort.deleteByPolicyId(policyId);
    }

    private void verifyPolicyAccess(UUID identityId, String role, UUID policyId) {
        // Enforces policy access rules using existing scope mechanisms
        policyService.getPolicy(identityId, role, policyId);
    }

    private String generateStorageKey(UUID policyId, String originalFilename) {
        // Sanitizing filename briefly to prevent path traversal issues.
        String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
        return "policies/" + policyId + "/" + UUID.randomUUID() + "-" + safeFilename;
    }

    public record PresignedUploadInfo(String storageKey, String uploadUrl) {}
    public record DocumentDownloadInfo(PolicyDocument document, String downloadUrl) {}
}
