package com.anverraglobal.policy.adapter.outbound.storage;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CloudflareR2AdapterTest {

    @Test
    void shouldGenerateUploadUrl() {
        CloudflareR2Properties props = new CloudflareR2Properties();
        props.setEndpoint("https://test.r2.cloudflarestorage.com");
        props.setRegion("auto");
        props.setAccessKey("test-access");
        props.setSecretKey("test-secret");
        props.setBucket("my-bucket");
        props.setPresignDuration(Duration.ofMinutes(15));

        CloudflareR2Adapter adapter = new CloudflareR2Adapter(props);

        String uploadUrl = adapter.generateUploadUrl("policies/123/doc.pdf", "application/pdf");

        assertThat(uploadUrl).isNotNull();
        assertThat(uploadUrl).contains("test.r2.cloudflarestorage.com");
        assertThat(uploadUrl).contains("my-bucket");
        assertThat(uploadUrl).contains("policies/123/doc.pdf");
        assertThat(uploadUrl).contains("X-Amz-Signature");
    }

    @Test
    void shouldGenerateDownloadUrl() {
        CloudflareR2Properties props = new CloudflareR2Properties();
        props.setEndpoint("https://test.r2.cloudflarestorage.com");
        props.setRegion("auto");
        props.setAccessKey("test-access");
        props.setSecretKey("test-secret");
        props.setBucket("my-bucket");
        props.setPresignDuration(Duration.ofMinutes(15));

        CloudflareR2Adapter adapter = new CloudflareR2Adapter(props);

        String downloadUrl = adapter.generateDownloadUrl("policies/123/doc.pdf");

        assertThat(downloadUrl).isNotNull();
        assertThat(downloadUrl).contains("test.r2.cloudflarestorage.com");
        assertThat(downloadUrl).contains("my-bucket");
        assertThat(downloadUrl).contains("policies/123/doc.pdf");
        assertThat(downloadUrl).contains("X-Amz-Signature");
    }

    @Test
    void shouldFailIfConfigurationIsMissing() {
        CloudflareR2Properties props = new CloudflareR2Properties();
        // endpoint is null
        props.setAccessKey("test-access");
        props.setSecretKey("test-secret");
        props.setBucket("my-bucket");

        CloudflareR2Adapter adapter = new CloudflareR2Adapter(props);

        assertThatThrownBy(() -> adapter.generateUploadUrl("policies/123/doc.pdf", "application/pdf"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("R2 configuration is incomplete");
    }
}
