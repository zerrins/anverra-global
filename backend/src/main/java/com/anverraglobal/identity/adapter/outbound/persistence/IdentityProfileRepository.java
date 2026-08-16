package com.anverraglobal.identity.adapter.outbound.persistence;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IdentityProfileRepository extends CrudRepository<IdentityProfileEntity, UUID> {

    @Query("SELECT identity_id, display_name, email, created_at, updated_at FROM identities WHERE identity_id IN (:identityIds)")
    List<IdentityProfileEntity> findByIdentityIds(@Param("identityIds") Set<UUID> identityIds);

    @Modifying
    @Query("""
        INSERT INTO identities (identity_id, display_name, email, created_at, updated_at) 
        VALUES (:identityId, :displayName, :email, :createdAt, :updatedAt)
        ON CONFLICT (identity_id) DO UPDATE SET 
            display_name = EXCLUDED.display_name,
            email = EXCLUDED.email,
            updated_at = EXCLUDED.updated_at
    """)
    void upsert(
        @Param("identityId") UUID identityId,
        @Param("displayName") String displayName,
        @Param("email") String email,
        @Param("createdAt") Instant createdAt,
        @Param("updatedAt") Instant updatedAt
    );
}
