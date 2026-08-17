package com.anverraglobal.policy.adapter.outbound.storage;

import com.anverraglobal.policy.port.outbound.DocumentStoragePort;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.time.Duration;

/**
 * Cloudflare R2 specific implementation of DocumentStoragePort.
 * Implements S3-compatible API using software.amazon.awssdk.
 */
@Component
public class CloudflareR2Adapter implements DocumentStoragePort {

    private final CloudflareR2Properties properties;
    private S3Presigner presigner;
    private S3Client s3Client;

    public CloudflareR2Adapter(CloudflareR2Properties properties) {
        this.properties = properties;
    }

    private synchronized void initClients() {
        if (s3Client != null && presigner != null) {
            return;
        }

        if (properties.getEndpoint() == null || properties.getAccessKey() == null || properties.getSecretKey() == null || properties.getBucket() == null) {
            throw new IllegalStateException("R2 configuration is incomplete");
        }

        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey())
        );

        this.presigner = S3Presigner.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(credentialsProvider)
                .build();

        this.s3Client = S3Client.builder()
                .endpointOverride(URI.create(properties.getEndpoint()))
                .region(Region.of(properties.getRegion()))
                .credentialsProvider(credentialsProvider)
                // R2 requires path style addressing for some operations, but typically works with virtual host if endpoint is right.
                // We enable path style to be safe with S3-compatible APIs like R2
                .serviceConfiguration(S3Configuration.builder().pathStyleAccessEnabled(true).build())
                .build();
    }

    @Override
    public String generateUploadUrl(String storageKey, String contentType) {
        initClients();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(storageKey)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(properties.getPresignDuration())
                .putObjectRequest(putObjectRequest)
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }

    @Override
    public String generateDownloadUrl(String storageKey) {
        initClients();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(storageKey)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(properties.getPresignDuration())
                .getObjectRequest(getObjectRequest)
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }

    @Override
    public void removeDocument(String storageKey) {
        initClients();
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(properties.getBucket())
                .key(storageKey)
                .build();

        s3Client.deleteObject(deleteRequest);
    }
}
