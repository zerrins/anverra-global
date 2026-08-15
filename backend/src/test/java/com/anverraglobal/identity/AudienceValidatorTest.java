package com.anverraglobal.identity;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceValidatorTest {

    private final AudienceValidator validator = new AudienceValidator("anverra-api");

    @Test
    void testValidAudience() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .audience(List.of("anverra-api", "other-api"))
                .build();

        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertFalse(result.hasErrors());
    }

    @Test
    void testInvalidAudience() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .audience(List.of("wrong-api"))
                .build();

        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
    }

    @Test
    void testMissingAudience() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("dummy", "value")
                .build();

        OAuth2TokenValidatorResult result = validator.validate(jwt);
        assertTrue(result.hasErrors());
    }
}
