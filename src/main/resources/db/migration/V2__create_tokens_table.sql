-- V2__create_tokens_table.sql
-- Migration #2: create table tokens

-- Creating table 'tokens' for storing main token data
CREATE TABLE IF NOT EXISTS tokens
(
    id                          BIGSERIAL PRIMARY KEY,
    user_id                     BIGINT NOT NULL REFERENCES users(id),

    token                       VARCHAR(255) NOT NULL,
    expired_token               TIMESTAMP WITH TIME ZONE NOT NULL,

    refresh_token               VARCHAR(255) NOT NULL,
    expired_refresh_token       TIMESTAMP WITH TIME ZONE NOT NULL,

    password_reset_token        VARCHAR(255),
    expired_password_reset_token TIMESTAMP WITH TIME ZONE,

    ip_address                  VARCHAR(45), -- IPv4/IPv6
    user_agent                  TEXT,
    revoked                     BOOLEAN DEFAULT FALSE,

    created_at                  TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at                  TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Indexes for faster lookups
CREATE INDEX idx_tokens_user_id ON tokens(user_id);
CREATE INDEX idx_tokens_token ON tokens(token);
CREATE INDEX idx_tokens_refresh_token ON tokens(refresh_token);
CREATE INDEX idx_tokens_password_reset_token ON tokens(password_reset_token);
CREATE INDEX idx_tokens_ip_address ON tokens(ip_address);

