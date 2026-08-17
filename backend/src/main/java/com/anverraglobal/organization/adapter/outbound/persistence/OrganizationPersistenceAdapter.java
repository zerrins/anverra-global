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
    public List<com.anverraglobal.organization.domain.Dealer> findAllDealers() {
        return StreamSupport.stream(dealerRepository.findAll().spliterator(), false)
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<com.anverraglobal.organization.domain.Dealer> findDealerById(UUID dealerId) {
        return dealerRepository.findById(dealerId)
                .map(this::mapToDomain);
    }

    @Override
    public List<com.anverraglobal.organization.domain.Branch> findBranchesByDealer(UUID dealerId) {
        return branchRepository.findByDealerId(dealerId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<com.anverraglobal.organization.domain.Branch> findBranchById(UUID branchId) {
        return branchRepository.findById(branchId)
                .map(this::mapToDomain);
    }

    @Override
    public void saveDealer(com.anverraglobal.organization.domain.Dealer dealer) {
        DealerRecord record = new DealerRecord();
        record.setId(dealer.getId());
        record.setName(dealer.getName());
        record.setStatus(dealer.getStatus().name());
        record.setVersion(dealer.getVersion());
        dealerRepository.save(record);
    }

    @Override
    public void saveBranch(com.anverraglobal.organization.domain.Branch branch) {
        BranchRecord record = new BranchRecord();
        record.setId(branch.getId());
        record.setDealerId(branch.getDealerId());
        record.setName(branch.getName());
        record.setStatus(branch.getStatus().name());
        record.setVersion(branch.getVersion());
        branchRepository.save(record);
    }

    private com.anverraglobal.organization.domain.Dealer mapToDomain(DealerRecord record) {
        return new com.anverraglobal.organization.domain.Dealer(
                record.getId(),
                record.getName(),
                record.getStatus() != null ? com.anverraglobal.organization.domain.OrganizationStatus.valueOf(record.getStatus()) : com.anverraglobal.organization.domain.OrganizationStatus.ACTIVE,
                record.getVersion()
        );
    }

    private com.anverraglobal.organization.domain.Branch mapToDomain(BranchRecord record) {
        return new com.anverraglobal.organization.domain.Branch(
                record.getId(),
                record.getDealerId(),
                record.getName(),
                record.getStatus() != null ? com.anverraglobal.organization.domain.OrganizationStatus.valueOf(record.getStatus()) : com.anverraglobal.organization.domain.OrganizationStatus.ACTIVE,
                record.getVersion()
        );
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
