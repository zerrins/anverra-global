-- Organization Module Tables
CREATE TABLE dealers (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE branches (
    id UUID PRIMARY KEY,
    dealer_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
    -- Logical FK to dealers
);

CREATE TABLE organization_memberships (
    id UUID PRIMARY KEY,
    identity_id UUID NOT NULL,
    role VARCHAR(50) NOT NULL,
    branch_id UUID,
    dealer_id UUID,
    parent_identity_id UUID,
    version BIGINT NOT NULL DEFAULT 0
    -- Logical FK to branches or dealers
);

-- Policy Module Tables
CREATE TABLE policies (
    id UUID PRIMARY KEY,
    policy_number VARCHAR(50) NOT NULL UNIQUE,
    created_by UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    customer_id UUID NOT NULL,
    agent_a_id UUID,
    agent_b_id UUID,
    branch_id UUID,
    premium NUMERIC(19, 4) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE policy_documents (
    policy_id UUID PRIMARY KEY,
    storage_key VARCHAR(512) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

-- Commission Module Tables
CREATE TABLE commissions (
    policy_id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(20),
    total_commission_value NUMERIC(19, 4),
    agent_a_share NUMERIC(19, 4),
    agent_b_share NUMERIC(19, 4),
    version BIGINT NOT NULL DEFAULT 0
);

-- Reporting Module Tables
CREATE TABLE reporting_policy_read_models (
    policy_id UUID PRIMARY KEY,
    policy_number VARCHAR(50) NOT NULL,
    customer_id UUID NOT NULL,
    agent_a_id UUID,
    agent_b_id UUID,
    branch_id UUID,
    premium NUMERIC(19, 4) NOT NULL,
    status VARCHAR(20) NOT NULL,
    commission_status VARCHAR(20),
    commission_type VARCHAR(20),
    total_commission_value NUMERIC(19, 4),
    agent_a_share NUMERIC(19, 4),
    agent_b_share NUMERIC(19, 4),
    policy_aggregate_version BIGINT NOT NULL DEFAULT 0,
    commission_aggregate_version BIGINT NOT NULL DEFAULT 0
);
