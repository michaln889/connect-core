-- USERS
ALTER TABLE users
ADD COLUMN is_deleted BOOLEAN;

UPDATE users SET is_deleted = FALSE WHERE is_deleted IS NULL;

ALTER TABLE users
ALTER COLUMN is_deleted SET NOT NULL,
ALTER COLUMN is_deleted SET DEFAULT FALSE;

CREATE INDEX idx_users_is_deleted ON users(is_deleted);



-- POSTS
ALTER TABLE posts
ADD COLUMN is_deleted BOOLEAN;

UPDATE posts SET is_deleted = FALSE WHERE is_deleted IS NULL;

ALTER TABLE posts
ALTER COLUMN is_deleted SET NOT NULL,
ALTER COLUMN is_deleted SET DEFAULT FALSE;

CREATE INDEX idx_posts_not_deleted
ON posts(user_id, created_at DESC)
WHERE is_deleted = FALSE;



-- MEDIA
ALTER TABLE media
ADD COLUMN is_deleted BOOLEAN;

UPDATE media SET is_deleted = FALSE WHERE is_deleted IS NULL;

ALTER TABLE media
ALTER COLUMN is_deleted SET NOT NULL,
ALTER COLUMN is_deleted SET DEFAULT FALSE;

CREATE INDEX idx_media_not_deleted
ON media(post_id)
WHERE is_deleted = FALSE;



-- MESSAGES
ALTER TABLE messages
ADD COLUMN deleted_by_sender BOOLEAN,
ADD COLUMN deleted_by_receiver BOOLEAN;

UPDATE messages
SET deleted_by_sender = FALSE,
    deleted_by_receiver = FALSE
WHERE deleted_by_sender IS NULL
   OR deleted_by_receiver IS NULL;

ALTER TABLE messages
ALTER COLUMN deleted_by_sender SET NOT NULL,
ALTER COLUMN deleted_by_sender SET DEFAULT FALSE,
ALTER COLUMN deleted_by_receiver SET NOT NULL,
ALTER COLUMN deleted_by_receiver SET DEFAULT FALSE;

CREATE INDEX idx_messages_not_deleted_conversation
ON messages(sender_id, receiver_id, created_at DESC)
WHERE deleted_by_sender = FALSE OR deleted_by_receiver = FALSE;