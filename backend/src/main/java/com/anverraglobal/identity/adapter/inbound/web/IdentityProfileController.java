package com.anverraglobal.identity.adapter.inbound.web;

import com.anverraglobal.identity.application.IdentityProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/identity")
public class IdentityProfileController {

    private static final Logger log = LoggerFactory.getLogger(IdentityProfileController.class);
    private final IdentityProfileService identityProfileService;

    public IdentityProfileController(IdentityProfileService identityProfileService) {
        this.identityProfileService = identityProfileService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Void> syncProfile(@RequestBody IdentitySyncRequest request) {
        if (request.identityId() == null || request.displayName() == null || request.displayName().isBlank()) {
            log.warn("Attempted to sync profile with missing required fields");
            return ResponseEntity.badRequest().build();
        }

        try {
            identityProfileService.upsertProfile(request.identityId(), request.displayName(), request.email());
        } catch (Exception e) {
            log.error("Failed to synchronize identity profile for id: {}", request.identityId(), e);
            // We return 200 OK or 202 Accepted even on failure so that Auth0 Action doesn't fail the login flow,
            // as per "Profile synchronization failures must not invalidate an otherwise successful authentication"
            // But from the REST perspective, returning 500 might cause the Action to fail if we don't catch it there.
            // Let's return 202 Accepted to signal it was received but maybe failed processing, or 200 OK.
            return ResponseEntity.ok().build(); 
        }

        return ResponseEntity.ok().build();
    }

    public record IdentitySyncRequest(UUID identityId, String displayName, String email) {}
}
