-- Criar extensão para UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Criar schema se não existir
CREATE SCHEMA IF NOT EXISTS meetixdev;

-- Criar tabela de usuários
CREATE TABLE meetixdev.user_account (
    id_user UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    instagram VARCHAR(255),
    university VARCHAR(255),
    course VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Criar índice para otimizar busca por email
CREATE INDEX IF NOT EXISTS idx_user_account_email ON meetixdev.user_account(email);

-- Trigger para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION meetixdev.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_user_account_updated_at
    BEFORE UPDATE ON meetixdev.user_account
    FOR EACH ROW
    EXECUTE FUNCTION meetixdev.update_updated_at_column();
