-- STEP 15L: Policy product association, dates, and sum assured
-- Adds product_id, effective_date, expiry_date, sum_assured to policies table.
-- All columns are NULLABLE to preserve legacy policy integrity (REQ-DEC-011 §6, HD-1 approved).
-- Date columns use DATE type per HD-3 (business dates, not precise instants).
-- No physical FK to products — bounded-context isolation (REQ-DEC-011 §10).

ALTER TABLE policies ADD COLUMN product_id UUID NULL;
ALTER TABLE policies ADD COLUMN effective_date DATE NULL;
ALTER TABLE policies ADD COLUMN expiry_date DATE NULL;
ALTER TABLE policies ADD COLUMN sum_assured NUMERIC(19, 4) NULL;

CREATE INDEX idx_policies_product_id ON policies(product_id);

-- Extend the reporting read model with the same fields so the event-driven
-- read model can propagate product, dates and sum assured from Policy events.
ALTER TABLE reporting_policy_read_models ADD COLUMN product_id UUID NULL;
ALTER TABLE reporting_policy_read_models ADD COLUMN effective_date DATE NULL;
ALTER TABLE reporting_policy_read_models ADD COLUMN expiry_date DATE NULL;
ALTER TABLE reporting_policy_read_models ADD COLUMN sum_assured NUMERIC(19, 4) NULL;
