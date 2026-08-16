package com.anverraglobal.identity.contracts;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface IdentityProfileContract {

    /**
     * Resolves human-readable display names for a set of identity UUIDs.
     * @param identityIds the set of identity UUIDs
     * @return a map of identity UUIDs to their display names. Missing IDs are omitted from the map.
     */
    Map<UUID, String> resolveDisplayNames(Set<UUID> identityIds);
}
