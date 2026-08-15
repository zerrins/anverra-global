package com.anverraglobal.identity;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String IDENTITY_ID_CLAIM = "https://anverraglobal.com/identity_id";
    private static final String ROLES_CLAIM = "https://anverraglobal.com/roles";

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        // 1. Extract and validate identity_id
        String identityIdStr = jwt.getClaimAsString(IDENTITY_ID_CLAIM);
        if (identityIdStr == null || identityIdStr.trim().isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Missing required custom claim: " + IDENTITY_ID_CLAIM, null));
        }

        UUID identityId;
        try {
            identityId = UUID.fromString(identityIdStr);
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Malformed custom claim: " + IDENTITY_ID_CLAIM, null));
        }

        // 2. Extract and validate roles
        List<String> roles = jwt.getClaimAsStringList(ROLES_CLAIM);
        if (roles == null || roles.isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Missing or empty required custom claim: " + ROLES_CLAIM, null));
        }

        if (roles.size() > 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Ambiguous authorization context: exactly one role is required", null));
        }

        String role = roles.get(0);
        if (role == null || role.trim().isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Role cannot be empty", null));
        }

        // Standardize prefix
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // 3. Return JwtAuthenticationToken overriding the name with the UUID string
        return new JwtAuthenticationToken(jwt, authorities, identityId.toString());
    }
}
