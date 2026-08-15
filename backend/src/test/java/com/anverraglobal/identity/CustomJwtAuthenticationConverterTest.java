package com.anverraglobal.identity;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomJwtAuthenticationConverterTest {

    private final CustomJwtAuthenticationConverter converter = new CustomJwtAuthenticationConverter();

    @Test
    void testValidJwt() {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();

        AbstractAuthenticationToken token = converter.convert(jwt);
        
        assertEquals(uuid, token.getName());
        assertEquals(1, token.getAuthorities().size());
        assertTrue(token.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals("ROLE_AGENT")));
    }

    @Test
    void testMissingIdentityId() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();

        assertThrows(OAuth2AuthenticationException.class, () -> converter.convert(jwt));
    }

    @Test
    void testMalformedIdentityId() {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", "not-a-uuid")
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT"))
                .build();

        assertThrows(OAuth2AuthenticationException.class, () -> converter.convert(jwt));
    }

    @Test
    void testMultipleRolesFailClosed() {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .claim("https://anverraglobal.com/roles", List.of("ROLE_AGENT", "ROLE_CUSTOMER"))
                .build();

        assertThrows(OAuth2AuthenticationException.class, () -> converter.convert(jwt));
    }

    @Test
    void testZeroRolesFailClosed() {
        String uuid = UUID.randomUUID().toString();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("https://anverraglobal.com/identity_id", uuid)
                .claim("https://anverraglobal.com/roles", List.of())
                .build();

        assertThrows(OAuth2AuthenticationException.class, () -> converter.convert(jwt));
    }
}
