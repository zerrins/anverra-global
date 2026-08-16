CREATE TABLE customers (
    id UUID PRIMARY KEY,
    customer_type VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    contact_info TEXT NOT NULL,
    address_info TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    individual_info JSONB,
    business_info JSONB,
    dealer_id UUID NOT NULL,
    branch_id UUID NOT NULL,
    agent_id UUID NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE INDEX idx_customers_agent_id ON customers(agent_id);
CREATE INDEX idx_customers_branch_id ON customers(branch_id);
CREATE INDEX idx_customers_dealer_id ON customers(dealer_id);
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_customers_type_status ON customers(customer_type, status);
