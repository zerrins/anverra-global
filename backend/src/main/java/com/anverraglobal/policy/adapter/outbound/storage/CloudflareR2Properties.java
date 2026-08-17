package com.anverraglobal.policy.adapter.outbound.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@ConfigurationProperties(prefix = "r2")
public class CloudflareR2Properties {

    private String endpoint;
    private String region = "auto";
    private String accessKey;
    private String secretKey;
    private String bucket;
    private Duration presignDuration = Duration.ofMinutes(15);

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }

    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }

    public String getBucket() { return bucket; }
    public void setBucket(String bucket) { this.bucket = bucket; }

    public Duration getPresignDuration() { return presignDuration; }
    public void setPresignDuration(Duration presignDuration) { this.presignDuration = presignDuration; }
}
