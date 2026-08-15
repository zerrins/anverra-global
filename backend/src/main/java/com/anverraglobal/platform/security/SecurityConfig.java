package com.anverraglobal.platform.security;

import com.anverraglobal.identity.AudienceValidator;
import com.anverraglobal.identity.CustomJwtAuthenticationConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${anverra.security.oauth2.issuer-uri}")
    private String issuerUri;

    @Value("${anverra.security.oauth2.audience}")
    private String audience;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ProblemDetailAuthenticationEntryPoint entryPoint) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF strategy deferred to implementation per D06/O14
            .exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(jwtDecoder())
                    .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                )
                .authenticationEntryPoint(entryPoint) // Ensure 401s use the ProblemDetail handler
            );
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Use withJwkSetUri to prevent eager network calls to the OIDC discovery endpoint during application startup,
        // which allows tests using @WithMockUser to run without needing an internet connection.
        String jwkSetUri = issuerUri.endsWith("/") ? issuerUri + ".well-known/jwks.json" : issuerUri + "/.well-known/jwks.json";
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}
