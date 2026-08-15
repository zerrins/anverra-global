package com.anverraglobal.commission.contracts;

import java.util.UUID;

/**
 * Public contract for managing commissions.
 * Called by Policy application layer during cross-module transactions.
 */
public interface CommissionManagementService {

    /**
     * Resets the commission for the given policy to UNSET.
     * Must be called within an existing transaction to ensure atomicity.
     * Publishes a CommissionConfiguredEvent (UNSET).
     *
     * @param policyId the identity of the policy whose commission should be reset
     */
    void resetToUnset(UUID policyId);

    /**
     * Configures the commission for the given policy.
     * Must be called within a transaction by the application layer.
     * Publishes a CommissionConfiguredEvent upon successful persistence.
     *
     * @param policyId the identity of the policy
     * @param commissionType the type of the commission (e.g. FIXED, PERCENTAGE)
     * @param totalCommissionValue the total value of the commission
     * @param agentAShare the share allocated to Agent A
     * @param agentBShare the share allocated to Agent B
     * @param policyPremium the premium of the policy, used for validation
     */
    void configureCommission(UUID policyId, String commissionType, java.math.BigDecimal totalCommissionValue, java.math.BigDecimal agentAShare, java.math.BigDecimal agentBShare, java.math.BigDecimal policyPremium);

    /**
     * Authoritatively checks whether a commission is configured for the given policy.
     * Returns true if the Commission aggregate exists and its status is CONFIGURED.
     * 
     * @param policyId the identity of the policy
     * @return true if configured, false if UNSET or missing
     */
    boolean isCommissionConfigured(UUID policyId);
}
