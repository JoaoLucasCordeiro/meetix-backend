CREATE SCHEMA IF NOT EXISTS meetixdev;

CREATE TABLE meetixdev.users (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    sobrenome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    instagram VARCHAR(255),
    faculdade VARCHAR(255),
    curso VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índice para otimizar busca por email
CREATE INDEX IF NOT EXISTS idx_users_email ON meetixdev.users(email);

-- Trigger para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION meetixdev.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON meetixdev.users 
    FOR EACH ROW 
    EXECUTE FUNCTION meetixdev.update_updated_at_column();