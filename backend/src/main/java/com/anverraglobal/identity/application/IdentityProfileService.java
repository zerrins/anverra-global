package com.anverraglobal.identity.application;

import com.anverraglobal.identity.application.port.out.IdentityProfilePersistencePort;
import com.anverraglobal.identity.contracts.IdentityProfileContract;
import com.anverraglobal.identity.domain.IdentityProfile;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class IdentityProfileService implements IdentityProfileContract {

    private final IdentityProfilePersistencePort persistencePort;

    public IdentityProfileService(IdentityProfilePersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    public void upsertProfile(UUID identityId, String displayName, String email) {
        IdentityProfile profile = new IdentityProfile(identityId, displayName, email, Instant.now(), Instant.now());
        persistencePort.upsert(profile);
    }

    @Override
    public Map<UUID, String> resolveDisplayNames(Set<UUID> identityIds) {
        return persistencePort.resolveDisplayNames(identityIds);
    }
}
