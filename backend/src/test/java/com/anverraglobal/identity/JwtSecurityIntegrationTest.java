package com.anverraglobal.identity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class JwtSecurityIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void testValidJwt() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/policy")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(result -> org.junit.jupiter.api.Assertions.assertNotEquals(401, result.getResponse().getStatus(), "Authentication should succeed, so status must not be 401"));
    }

    @Test
    void testMissingIdentityId() throws Exception {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/policy")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testMalformedIdentityId() throws Exception {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", "malformed-uuid")
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/policy")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testMissingRoles() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/policy")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testMultipleRoles() throws Exception {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT", "ROLE_CUSTOMER"))
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mockMvc.perform(get("/api/v1/policy")
                .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isUnauthorized());
    }
}
