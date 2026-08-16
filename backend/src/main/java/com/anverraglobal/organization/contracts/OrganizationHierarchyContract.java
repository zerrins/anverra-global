package com.anverraglobal.organization.contracts;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationHierarchyContract {
    Optional<HierarchyInfo> getHierarchyForIdentity(UUID identityId);

    record HierarchyInfo(UUID identityId, String role, UUID branchId, UUID dealerId, UUID parentIdentityId) {}
}
