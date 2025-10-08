-- Remover tabela existente se houver problemas
DROP TABLE IF EXISTS event_participant CASCADE;

-- Criar tabela de participantes de eventos com estrutura correta
CREATE TABLE event_participant (
    id_event_participant UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    event_id UUID NOT NULL,
    user_id UUID NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attended BOOLEAN DEFAULT FALSE,
    checked_in_at TIMESTAMP,
    CONSTRAINT fk_event_participant_event FOREIGN KEY (event_id) REFERENCES event(id_event) ON DELETE CASCADE,
    CONSTRAINT fk_event_participant_user FOREIGN KEY (user_id) REFERENCES user_account(id_user) ON DELETE CASCADE,
    CONSTRAINT unique_event_user UNIQUE (event_id, user_id)
);

-- Índices para otimização
CREATE INDEX IF NOT EXISTS idx_event_participant_event_id ON event_participant(event_id);
CREATE INDEX IF NOT EXISTS idx_event_participant_user_id ON event_participant(user_id);
CREATE INDEX IF NOT EXISTS idx_event_participant_registration_date ON event_participant(registration_date);