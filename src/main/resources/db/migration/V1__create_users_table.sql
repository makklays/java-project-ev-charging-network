-- V1__create_users_table.sql
-- Migration #1: create table users

-- Creating table 'users' for storing main user data
CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(255),
    email       VARCHAR(200) NOT NULL,
    base_role   VARCHAR(50) NOT NULL DEFAULT 'USER',
    mobile      VARCHAR(20),

    -- Data
    nickname    VARCHAR(100) NOT NULL,
    gender      VARCHAR(20),      -- MALE, FEMALE

    avatar_url  VARCHAR(255),     -- from S3
    birth_date  DATE,             -- 14.08.2026
    bio         TEXT,             -- О себе

    -- DRIVER (водитель), PASSENGER (хочу кататься), VIP (известный пользователь)
    status VARCHAR(50) NOT NULL DEFAULT 'DRIVER',

    password    VARCHAR(255),

    created_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT now(),

    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_username UNIQUE (username)
);

-- Adding index for fast searching by email
CREATE INDEX idx_users_email ON users(email);
-- Adding index for fast searching by mobile
CREATE INDEX idx_users_mobile ON users(mobile);

