ALTER TABLE policy_documents
ADD COLUMN content_type VARCHAR(255),
ADD COLUMN size_bytes BIGINT;
