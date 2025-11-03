CREATE TABLE IF NOT EXISTS event_admin (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id UUID NOT NULL,
    user_id UUID NOT NULL,
    invited_by UUID NOT NULL,
    invited_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted BOOLEAN NOT NULL DEFAULT FALSE,
    accepted_at TIMESTAMP,
    
    CONSTRAINT fk_event_admin_event FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_admin_user FOREIGN KEY (user_id) REFERENCES user_account(id) ON DELETE CASCADE,
    CONSTRAINT fk_event_admin_invited_by FOREIGN KEY (invited_by) REFERENCES user_account(id) ON DELETE CASCADE,
    
    CONSTRAINT uk_event_admin_event_user UNIQUE (event_id, user_id)
);

CREATE INDEX idx_event_admin_event_id ON event_admin(event_id);
CREATE INDEX idx_event_admin_user_id ON event_admin(user_id);
CREATE INDEX idx_event_admin_accepted ON event_admin(accepted);
