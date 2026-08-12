package com.anverraglobal.platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Basic scaffolding for the security foundation as required by D06.
        // specific authentication workflows, users, and authorization rules 
        // are intentionally deferred from this platform foundation step.
        http
            .csrf(csrf -> csrf.disable()) // CSRF strategy deferred to implementation per D06/O14
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
