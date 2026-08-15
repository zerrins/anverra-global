package com.anverraglobal.reporting.adapter.outbound.persistence;

import com.anverraglobal.organization.contracts.dto.OrganizationScope;
import com.anverraglobal.reporting.application.dto.CommissionStatisticsResponse;
import com.anverraglobal.reporting.application.dto.PolicyStatisticsResponse;
import com.anverraglobal.reporting.application.port.outbound.ReportingOutboundPort;
import com.anverraglobal.commission.event.CommissionConfiguredEvent;
import com.anverraglobal.policy.event.PolicyActivatedEvent;
import com.anverraglobal.policy.event.PolicyCreatedEvent;
import com.anverraglobal.policy.event.PolicyDeactivatedEvent;
import com.anverraglobal.policy.event.PolicyPremiumUpdatedEvent;
import com.anverraglobal.policy.event.PolicyReactivatedEvent;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Set;

@Component
public class ReportingPersistenceAdapter implements ReportingOutboundPort {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ReportingPersistenceAdapter(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PolicyStatisticsResponse getPolicyStatistics(OrganizationScope scope) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                COUNT(*) AS totalPolicies,
                COUNT(*) FILTER (WHERE status = 'DRAFT') AS draftCount,
                COUNT(*) FILTER (WHERE status = 'ACTIVE') AS activeCount,
                COUNT(*) FILTER (WHERE status = 'INACTIVE') AS inactiveCount
            FROM reporting_policy_read_models
            WHERE 1=1
        """);
        
        MapSqlParameterSource params = new MapSqlParameterSource();
        appendScopeConditions(sql, params, scope);

        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> new PolicyStatisticsResponse(
                rs.getLong("totalPolicies"),
                rs.getLong("draftCount"),
                rs.getLong("activeCount"),
                rs.getLong("inactiveCount")
        ));
    }

    @Override
    public CommissionStatisticsResponse getCommissionStatistics(OrganizationScope scope) {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                COALESCE(SUM(total_commission_value), 0) AS totalCommissionAmount,
                COALESCE(SUM(agent_a_share), 0) AS agentACommissionAmount,
                COALESCE(SUM(agent_b_share), 0) AS agentBCommissionAmount,
                COUNT(*) AS configuredCommissionCount
            FROM reporting_policy_read_models
            WHERE commission_status = 'CONFIGURED'
        """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        appendScopeConditions(sql, params, scope);

        return jdbcTemplate.queryForObject(sql.toString(), params, (rs, rowNum) -> new CommissionStatisticsResponse(
                rs.getBigDecimal("totalCommissionAmount"),
                rs.getBigDecimal("agentACommissionAmount"),
                rs.getBigDecimal("agentBCommissionAmount"),
                rs.getLong("configuredCommissionCount")
        ));
    }

    private void appendScopeConditions(StringBuilder sql, MapSqlParameterSource params, OrganizationScope scope) {
        if (scope.isGlobalAdmin()) {
            return;
        } else if (!scope.allowedCustomerIds().isEmpty()) {
            sql.append(" AND customer_id IN (:allowedCustomerIds)");
            params.addValue("allowedCustomerIds", scope.allowedCustomerIds());
        } else if (!scope.allowedAgentIds().isEmpty()) {
            sql.append(" AND (agent_a_id IN (:allowedAgentIds) OR agent_b_id IN (:allowedAgentIds))");
            params.addValue("allowedAgentIds", scope.allowedAgentIds());
        } else if (!scope.allowedBranchIds().isEmpty()) {
            sql.append(" AND branch_id IN (:allowedBranchIds)");
            params.addValue("allowedBranchIds", scope.allowedBranchIds());
        } else {
            // Empty scope means no access to anything
            sql.append(" AND 1=0");
        }
    }

    @Override
    public void savePolicyCreatedEvent(PolicyCreatedEvent event) {
        String sql = """
            INSERT INTO reporting_policy_read_models (
                policy_id, policy_number, customer_id, agent_a_id, agent_b_id, branch_id, 
                premium, status, policy_aggregate_version, commission_aggregate_version
            ) VALUES (
                :policyId, :policyNumber, :customerId, :agentAId, :agentBId, :branchId, 
                :premium, :status, :policyAggregateVersion, 0
            ) ON CONFLICT (policy_id) DO UPDATE SET
                policy_number = EXCLUDED.policy_number,
                customer_id = EXCLUDED.customer_id,
                agent_a_id = EXCLUDED.agent_a_id,
                agent_b_id = EXCLUDED.agent_b_id,
                branch_id = EXCLUDED.branch_id,
                premium = EXCLUDED.premium,
                status = EXCLUDED.status,
                policy_aggregate_version = EXCLUDED.policy_aggregate_version
            WHERE reporting_policy_read_models.policy_aggregate_version < EXCLUDED.policy_aggregate_version
        """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("policyId", event.aggregateId())
                .addValue("policyNumber", event.policyNumber())
                .addValue("customerId", event.customerId())
                .addValue("agentAId", event.agentAId())
                .addValue("agentBId", event.agentBId())
                .addValue("branchId", event.branchId())
                .addValue("premium", event.premiumAmount())
                .addValue("status", event.policyStatus())
                .addValue("policyAggregateVersion", event.aggregateVersion());
                
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void savePolicyActivatedEvent(PolicyActivatedEvent event) {
        updatePolicyStatusAndVersion(event.aggregateId(), "ACTIVE", event.aggregateVersion());
    }

    @Override
    public void savePolicyDeactivatedEvent(PolicyDeactivatedEvent event) {
        updatePolicyStatusAndVersion(event.aggregateId(), "INACTIVE", event.aggregateVersion());
    }

    @Override
    public void savePolicyReactivatedEvent(PolicyReactivatedEvent event) {
        updatePolicyStatusAndVersion(event.aggregateId(), "ACTIVE", event.aggregateVersion());
    }

    private void updatePolicyStatusAndVersion(UUID policyId, String status, Long aggregateVersion) {
        String sql = """
            UPDATE reporting_policy_read_models SET
                status = :status,
                policy_aggregate_version = :policyAggregateVersion
            WHERE policy_id = :policyId 
              AND policy_aggregate_version < :policyAggregateVersion
        """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("policyId", policyId)
                .addValue("status", status)
                .addValue("policyAggregateVersion", aggregateVersion);
                
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void savePolicyPremiumUpdatedEvent(PolicyPremiumUpdatedEvent event) {
        String sql = """
            UPDATE reporting_policy_read_models SET
                premium = :premium,
                policy_aggregate_version = :policyAggregateVersion
            WHERE policy_id = :policyId 
              AND policy_aggregate_version < :policyAggregateVersion
        """;
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("policyId", event.aggregateId())
                .addValue("premium", event.premiumAmount())
                .addValue("policyAggregateVersion", event.aggregateVersion());
                
        jdbcTemplate.update(sql, params);
    }

    @Override
    public void saveCommissionConfiguredEvent(CommissionConfiguredEvent event) {
        // Due to strict NOT NULL constraints on policy_number, customer_id, premium, and status
        // it is impossible to perform a safe INSERT for an out-of-order CommissionConfiguredEvent
        // without fabricating fake business data.
        String sql = """
            UPDATE reporting_policy_read_models SET
                commission_status = :commissionStatus,
                commission_type = :commissionType,
                total_commission_value = :totalCommissionValue,
                agent_a_share = :agentAShare,
                agent_b_share = :agentBShare,
                commission_aggregate_version = :commissionAggregateVersion
            WHERE policy_id = :policyId 
              AND commission_aggregate_version < :commissionAggregateVersion
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("policyId", event.aggregateId())
                .addValue("commissionStatus", event.commissionStatus())
                .addValue("commissionType", event.commissionType())
                .addValue("totalCommissionValue", event.totalCommissionValue())
                .addValue("agentAShare", event.agentAShare())
                .addValue("agentBShare", event.agentBShare())
                .addValue("commissionAggregateVersion", event.aggregateVersion());

        jdbcTemplate.update(sql, params);
    }
}
