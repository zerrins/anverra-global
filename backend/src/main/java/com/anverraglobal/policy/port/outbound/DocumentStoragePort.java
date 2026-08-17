package com.anverraglobal.policy.port.outbound;

import java.util.UUID;

/**
 * Provider-agnostic outbound port for document storage.
 * The Policy domain depends on this interface, not the actual storage implementation.
 */
public interface DocumentStoragePort {

    /**
     * Generates a pre-signed URL for document upload.
     * @param storageKey the intended storage key for the document
     * @param contentType the MIME type of the document
     * @return a signed URL string
     */
    String generateUploadUrl(String storageKey, String contentType);

    /**
     * Generates a pre-signed URL for document download.
     * @param storageKey the document's storage key
     * @return a signed URL string
     */
    String generateDownloadUrl(String storageKey);

    /**
     * Removes a document from storage.
     * @param storageKey the document's storage key
     */
    void removeDocument(String storageKey);
}
