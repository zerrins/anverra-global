package com.anverraglobal.identity.adapter.outbound.persistence;

import com.anverraglobal.identity.application.port.out.IdentityProfilePersistencePort;
import com.anverraglobal.identity.domain.IdentityProfile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class IdentityPersistenceAdapter implements IdentityProfilePersistencePort {

    private final IdentityProfileRepository repository;

    public IdentityPersistenceAdapter(IdentityProfileRepository repository) {
        this.repository = repository;
    }

    public void upsert(IdentityProfile profile) {
        repository.upsert(
            profile.getIdentityId(),
            profile.getDisplayName(),
            profile.getEmail(),
            profile.getCreatedAt(),
            profile.getUpdatedAt()
        );
    }

    public Map<UUID, String> resolveDisplayNames(Set<UUID> identityIds) {
        if (identityIds == null || identityIds.isEmpty()) {
            return Map.of();
        }
        return repository.findByIdentityIds(identityIds)
            .stream()
            .collect(Collectors.toMap(
                IdentityProfileEntity::getIdentityId,
                IdentityProfileEntity::getDisplayName
            ));
    }
}
