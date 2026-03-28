CREATE SCHEMA IF NOT EXISTS discodeit AUTHORIZATION discodeit_user;
GRANT USAGE ON SCHEMA discodeit TO discodeit_user;
GRANT CREATE ON SCHEMA discodeit TO discodeit_user;
ALTER ROLE discodeit_user SET SEARCH_PATH TO discodeit, public;
SET SEARCH_PATH to discodeit, public;

CREATE TABLE IF NOT EXISTS binary_contents
(
     id UUID PRIMARY KEY,
     created_at TIMESTAMPTZ NOT NULL,
     file_name VARCHAR(255) NOT NULL,
     size BIGINT NOT NULL,
     content_type VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE ,
    email VARCHAR(100) NOT NULL UNIQUE ,
    password VARCHAR(60) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    profile_id UUID UNIQUE,
    role VARCHAR(20) NOT NULL,
    CONSTRAINT fk_users_binary_content FOREIGN KEY (profile_id) REFERENCES binary_contents(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS persistent_logins (
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS channels
(
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE IF NOT EXISTS messages
(
    id UUID PRIMARY KEY,
    content TEXT NOT NULL,
    author_id UUID,
    channel_id UUID NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_author FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_message_attachments_message FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment FOREIGN KEY (attachment_id) REFERENCES binary_contents(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_statuses
(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_user_statuses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS read_statuses
(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_read_statuses_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT uk_read_statuses_user_channel UNIQUE (user_id, channel_id)
);

CREATE INDEX IF NOT EXISTS idx_messages_channel_created ON messages(channel_id, created_at DESC);