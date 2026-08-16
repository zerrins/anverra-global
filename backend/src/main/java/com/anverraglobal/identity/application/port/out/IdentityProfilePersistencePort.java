package com.anverraglobal.identity.application.port.out;

import com.anverraglobal.identity.domain.IdentityProfile;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IdentityProfilePersistencePort {
    void upsert(IdentityProfile profile);
    Map<UUID, String> resolveDisplayNames(Set<UUID> identityIds);
}
