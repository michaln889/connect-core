CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,

    is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE,

    CONSTRAINT fk_subscription_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE INDEX idx_subscription_user_id ON subscriptions(user_id);


CREATE TABLE posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,

    content TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE INDEX idx_posts_user_created_at
ON posts(user_id, created_at DESC);


CREATE TABLE media (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    user_id BIGINT,

    url TEXT NOT NULL,
    type VARCHAR(20) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_media_post
        FOREIGN KEY (post_id)
        REFERENCES posts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_media_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT media_owner_check
        CHECK (post_id IS NOT NULL OR user_id IS NOT NULL)
);
CREATE INDEX idx_media_post_id ON media(post_id);
CREATE INDEX idx_media_user_id ON media(user_id);


CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,

    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,

    content TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_message_receiver
        FOREIGN KEY (receiver_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE INDEX idx_messages_sender_receiver_created
ON messages(sender_id, receiver_id, created_at DESC);
CREATE INDEX idx_messages_receiver_sender_created
ON messages(receiver_id, sender_id, created_at DESC);
CREATE INDEX idx_messages_receiver_read
ON messages(receiver_id, is_read);


CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,
    type VARCHAR(50),
    message TEXT,

    is_read BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
CREATE INDEX idx_notifications_user_read_created
ON notifications(user_id, is_read, created_at DESC);


CREATE TABLE follows (
    id BIGSERIAL PRIMARY KEY,

    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_follows_follower
        FOREIGN KEY (follower_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_follows_following
        FOREIGN KEY (following_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT unique_follow
        UNIQUE (follower_id, following_id),

    CONSTRAINT no_self_follow
        CHECK (follower_id <> following_id)
);
CREATE INDEX idx_follows_follower_following
ON follows(follower_id, following_id);
CREATE INDEX idx_follows_following_follower
ON follows(following_id, follower_id);
