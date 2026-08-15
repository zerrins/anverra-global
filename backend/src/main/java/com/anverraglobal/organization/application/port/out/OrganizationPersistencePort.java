package com.anverraglobal.organization.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationPersistencePort {
    /**
     * Finds the organization memberships for the given identity.
     * @return a list of memberships, should typically be 0 or 1.
     */
    List<OrganizationMembershipDto> findMembershipsByIdentity(UUID identityId);

    /**
     * Finds all branch IDs associated with a specific dealer.
     */
    List<UUID> findBranchIdsByDealer(UUID dealerId);

    class OrganizationMembershipDto {
        private final UUID identityId;
        private final String role;
        private final UUID branchId;
        private final UUID dealerId;
        private final UUID parentIdentityId;

        public OrganizationMembershipDto(UUID identityId, String role, UUID branchId, UUID dealerId, UUID parentIdentityId) {
            this.identityId = identityId;
            this.role = role;
            this.branchId = branchId;
            this.dealerId = dealerId;
            this.parentIdentityId = parentIdentityId;
        }

        public UUID getIdentityId() {
            return identityId;
        }

        public String getRole() {
            return role;
        }

        public UUID getBranchId() {
            return branchId;
        }

        public UUID getDealerId() {
            return dealerId;
        }

        public UUID getParentIdentityId() {
            return parentIdentityId;
        }
    }
}
