CREATE TABLE IF NOT EXISTS outbox (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    aggregate_id VARCHAR(255),
    aggregate_type VARCHAR(255),
    event_type VARCHAR(255),
    payload JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    processed_at TIMESTAMP NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_PROCESSED',
    retry_count INT NOT NULL DEFAULT 0,
    last_error TEXT
);
