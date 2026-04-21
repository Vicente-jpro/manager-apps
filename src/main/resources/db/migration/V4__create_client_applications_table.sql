CREATE TABLE IF NOT EXISTS client_applications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES clients(id_client),
    app_id UUID NOT NULL REFERENCES applications(id_app),
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE(client_id, app_id)
);
