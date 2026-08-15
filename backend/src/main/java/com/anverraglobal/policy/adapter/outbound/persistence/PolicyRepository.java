package com.anverraglobal.policy.adapter.outbound.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PolicyRepository extends CrudRepository<PolicyEntity, UUID>, PagingAndSortingRepository<PolicyEntity, UUID> {
    
    Optional<PolicyEntity> findByPolicyNumber(String policyNumber);

    boolean existsByPolicyNumber(String policyNumber);

    Optional<PolicyEntity> findByPolicyNumberAndCustomerIdIn(String policyNumber, Set<UUID> customerIds);

    @org.springframework.data.jdbc.repository.query.Query("SELECT * FROM policies WHERE policy_number = :policyNumber AND (agent_a_id IN (:agentIds) OR agent_b_id IN (:agentIds))")
    Optional<PolicyEntity> findByPolicyNumberAndAgentIdsIn(@org.springframework.data.repository.query.Param("policyNumber") String policyNumber, @org.springframework.data.repository.query.Param("agentIds") Set<UUID> agentIds);

    Optional<PolicyEntity> findByPolicyNumberAndBranchIdIn(String policyNumber, Set<UUID> branchIds);


    Page<PolicyEntity> findByCustomerIdIn(Set<UUID> customerIds, Pageable pageable);

    Page<PolicyEntity> findByAgentAIdInOrAgentBIdIn(Set<UUID> agentAIds, Set<UUID> agentBIds, Pageable pageable);

    Page<PolicyEntity> findByBranchIdIn(Set<UUID> branchIds, Pageable pageable);

    Optional<PolicyEntity> findByIdAndCustomerIdIn(UUID id, Set<UUID> customerIds);

    @org.springframework.data.jdbc.repository.query.Query("SELECT * FROM policies WHERE id = :id AND (agent_a_id IN (:agentIds) OR agent_b_id IN (:agentIds))")
    Optional<PolicyEntity> findByIdAndAgentIdsIn(@org.springframework.data.repository.query.Param("id") UUID id, @org.springframework.data.repository.query.Param("agentIds") Set<UUID> agentIds);

    Optional<PolicyEntity> findByIdAndBranchIdIn(UUID id, Set<UUID> branchIds);
}
