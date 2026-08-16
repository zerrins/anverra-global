package com.anverraglobal.organization.adapter.outbound.persistence;

import com.anverraglobal.organization.application.port.out.OrganizationPersistencePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class OrganizationPersistenceAdapter implements OrganizationPersistencePort {

    private final OrganizationMembershipRepository membershipRepository;
    private final BranchRepository branchRepository;
    private final DealerRepository dealerRepository;

    public OrganizationPersistenceAdapter(OrganizationMembershipRepository membershipRepository, BranchRepository branchRepository, DealerRepository dealerRepository) {
        this.membershipRepository = membershipRepository;
        this.branchRepository = branchRepository;
        this.dealerRepository = dealerRepository;
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


    @Override
    public List<DealerDto> findAllDealers() {
        return StreamSupport.stream(dealerRepository.findAll().spliterator(), false)
                .map(d -> new DealerDto(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<DealerDto> findDealerById(UUID dealerId) {
        return dealerRepository.findById(dealerId)
                .map(d -> new DealerDto(d.getId(), d.getName()));
    }

    @Override
    public List<BranchDto> findBranchesByDealer(UUID dealerId) {
        return branchRepository.findByDealerId(dealerId).stream()
                .map(b -> new BranchDto(b.getId(), b.getName(), b.getDealerId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BranchDto> findBranchById(UUID branchId) {
        return branchRepository.findById(branchId)
                .map(b -> new BranchDto(b.getId(), b.getName(), b.getDealerId()));
    }

    @Override
    public List<UUID> findAgentIdsByBranch(UUID branchId) {
        return StreamSupport.stream(membershipRepository.findAll().spliterator(), false)
                .filter(m -> branchId.equals(m.getBranchId()) && "AGENT".equals(m.getRole()))
                .map(OrganizationMembershipRecord::getIdentityId)
                .distinct()
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
