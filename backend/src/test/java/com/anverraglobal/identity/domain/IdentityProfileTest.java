package com.anverraglobal.identity.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentityProfileTest {

    @Test
    void testValidIdentityProfile() {
        UUID id = UUID.randomUUID();
        Instant now = Instant.now();
        IdentityProfile profile = new IdentityProfile(id, "John Doe", "john@example.com", now, now);

        assertThat(profile.getIdentityId()).isEqualTo(id);
        assertThat(profile.getDisplayName()).isEqualTo("John Doe");
        assertThat(profile.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void testInvalidIdentityId() {
        assertThatThrownBy(() -> new IdentityProfile(null, "John Doe", "john@example.com", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("identityId must not be null");
    }

    @Test
    void testInvalidDisplayName() {
        UUID id = UUID.randomUUID();
        assertThatThrownBy(() -> new IdentityProfile(id, "   ", "john@example.com", null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("displayName must not be blank");
    }
}
