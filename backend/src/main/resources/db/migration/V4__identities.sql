-- V4__identities.sql
CREATE TABLE identities (
    identity_id UUID PRIMARY KEY,
    display_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
