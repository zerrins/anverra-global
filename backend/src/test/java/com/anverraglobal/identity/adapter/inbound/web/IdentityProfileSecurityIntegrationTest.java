package com.anverraglobal.identity.adapter.inbound.web;

import com.anverraglobal.identity.application.IdentityProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class IdentityProfileSecurityIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("spring.modulith.events.jdbc.schema-initialization.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IdentityProfileService identityProfileService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void syncProfile_M2MTokenWithScope_Succeeds() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "Agent Sync", "sync@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(jwt().jwt(builder -> builder
                        .claim("scope", "write:identity")
                ).authorities(new SimpleGrantedAuthority("SCOPE_write:identity"))))
                .andExpect(status().isOk());
    }

    @Test
    void syncProfile_M2MTokenWithoutScope_Forbidden() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "Agent Sync", "sync@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(jwt().jwt(builder -> builder
                        .claim("scope", "read:other")
                ).authorities(new SimpleGrantedAuthority("SCOPE_read:other"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void syncProfile_NormalUserJWT_Forbidden() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "Agent Sync", "sync@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .with(jwt().jwt(builder -> builder
                        .claim("https://anverraglobal.com/identity_id", UUID.randomUUID().toString())
                        .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                )))
                .andExpect(status().isForbidden());
    }

    @Test
    void syncProfile_Anonymous_Unauthorized() throws Exception {
        UUID id = UUID.randomUUID();
        IdentityProfileController.IdentitySyncRequest request = new IdentityProfileController.IdentitySyncRequest(id, "Agent Sync", "sync@example.com");

        mockMvc.perform(post("/api/v1/identity/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


}
