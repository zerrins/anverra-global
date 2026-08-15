package com.anverraglobal.policy.adapter.outbound.storage;

import com.anverraglobal.policy.port.outbound.DocumentStoragePort;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Cloudflare R2 specific implementation of DocumentStoragePort.
 * Implements S3-compatible API using software.amazon.awssdk.
 */
@Component
public class CloudflareR2Adapter implements DocumentStoragePort {

    // private final S3Presigner presigner;
    
    public CloudflareR2Adapter() {
        // Initialize AWS S3Presigner with R2 endpoint
    }

    @Override
    public String generateUploadUrl(UUID policyId) {
        // Use presigner to generate PUT URL
        return "https://r2.cloudflare.com/mock-upload/" + policyId;
    }

    @Override
    public String generateDownloadUrl(String storageKey) {
        // Use presigner to generate GET URL
        return "https://r2.cloudflare.com/mock-download/" + storageKey;
    }

    @Override
    public void removeDocument(String storageKey) {
        // Call S3 deleteObject
    }
}
