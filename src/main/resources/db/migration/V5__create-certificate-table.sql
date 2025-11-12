CREATE TABLE certificates (
    certificate_id UUID PRIMARY KEY,
    participant_id UUID NOT NULL,
    validation_code UUID NOT NULL,
    issue_date TIMESTAMP NOT NULL,

    CONSTRAINT uk_certificate_participant UNIQUE (participant_id),
    CONSTRAINT uk_certificate_validation_code UNIQUE (validation_code),

    CONSTRAINT fk_certificate_participant
        FOREIGN KEY (participant_id)
        REFERENCES event_participant (id)
        ON DELETE CASCADE
);