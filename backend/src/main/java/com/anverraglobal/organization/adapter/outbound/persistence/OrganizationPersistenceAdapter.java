package com.anverraglobal.organization.adapter.outbound.persistence;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrganizationPersistenceAdapter implements OrganizationPersistencePort {

    private final OrganizationMembershipRepository membershipRepository;
    private final BranchRepository branchRepository;

    public OrganizationPersistenceAdapter(OrganizationMembershipRepository membershipRepository, BranchRepository branchRepository) {
        this.membershipRepository = membershipRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public List<OrganizationMembershipDto> findMembershipsByIdentity(UUID identityId) {
        return membershipRepository.findByIdentityId(identityId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UUID> findBranchIdsByDealer(UUID dealerId) {
        return branchRepository.findByDealerId(dealerId).stream()
                .map(BranchRecord::getId)
                .collect(Collectors.toList());
    }

    private OrganizationMembershipDto mapToDto(OrganizationMembershipRecord record) {
        return new OrganizationMembershipDto(
                record.getIdentityId(),
                record.getRole(),
                record.getBranchId(),
                record.getDealerId(),
                record.getParentIdentityId()
        );
    }
}
