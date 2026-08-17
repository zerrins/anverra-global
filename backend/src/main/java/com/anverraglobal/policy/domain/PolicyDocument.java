package com.anverraglobal.policy.domain;

import java.util.UUID;

public class PolicyDocument {

    private UUID policyId;
    private String storageKey;
    private String originalFilename;
    private String contentType;
    private Long sizeBytes;
    private Long version;

    public PolicyDocument(UUID policyId, String storageKey, String originalFilename, String contentType, Long sizeBytes, Long version) {
        if (policyId == null) {
            throw new IllegalArgumentException("Policy ID must not be null");
        }
        if (storageKey == null || storageKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Storage key must not be blank");
        }
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Original filename must not be blank");
        }

        this.policyId = policyId;
        this.storageKey = storageKey;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.version = version;
    }

    public static PolicyDocument createNew(UUID policyId, String storageKey, String originalFilename, String contentType, Long sizeBytes) {
        if (contentType == null || contentType.trim().isEmpty()) {
            throw new IllegalArgumentException("Content type must not be blank for new documents");
        }
        if (sizeBytes == null || sizeBytes < 0) {
            throw new IllegalArgumentException("Size bytes must be zero or positive for new documents");
        }
        return new PolicyDocument(policyId, storageKey, originalFilename, contentType, sizeBytes, null);
    }

    public UUID getPolicyId() { return policyId; }
    public String getStorageKey() { return storageKey; }
    public String getOriginalFilename() { return originalFilename; }
    public String getContentType() { return contentType; }
    public Long getSizeBytes() { return sizeBytes; }
    public Long getVersion() { return version; }
}
