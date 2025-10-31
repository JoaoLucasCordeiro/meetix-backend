CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE event (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    location VARCHAR(500),
    img_url VARCHAR(500),
    event_url VARCHAR(500),
    remote BOOLEAN DEFAULT FALSE,
    max_attendees INTEGER,
    is_paid BOOLEAN DEFAULT FALSE,
    price NUMERIC(10, 2),
    organizer_id UUID NOT NULL,
    generate_certificate BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_organizer FOREIGN KEY (organizer_id) REFERENCES user_account(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_event_organizer_id ON event(organizer_id);
CREATE INDEX IF NOT EXISTS idx_event_start_date_time ON event(start_date_time);
CREATE INDEX IF NOT EXISTS idx_event_event_type ON event(event_type);
CREATE INDEX IF NOT EXISTS idx_event_remote ON event(remote);

CREATE TRIGGER update_event_updated_at
    BEFORE UPDATE ON event
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

ALTER TABLE event
    ADD CONSTRAINT check_event_location_or_url
        CHECK (
            (remote = TRUE AND event_url IS NOT NULL) OR
            (remote = FALSE AND location IS NOT NULL) OR
            (remote IS NULL)
        );
