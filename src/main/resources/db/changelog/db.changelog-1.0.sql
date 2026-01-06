--liquibase formatted sql

--changeset SPavelin:1
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL CHECK (LENGTH(password) >= 8),
                       first_name VARCHAR(255) NOT NULL,
                       last_name VARCHAR(255) NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT true,
                       roles text[] NOT NULL,
                       telegram_username varchar(50) default null,
                       telegram_chat_id varchar(50) default NULL
);

--changeset SPavelin:2
CREATE TABLE posts (
                       id UUID PRIMARY KEY,
                       user_id UUID NOT NULL,
                       content TEXT,
                       file_id VARCHAR(255),
                       created_at TIMESTAMP,

                        CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE

);

CREATE TABLE comments (
                          id UUID PRIMARY KEY,
                          post_id UUID REFERENCES posts(id),
                          user_id UUID NOT NULL,
                          content TEXT,
                          created_at TIMESTAMP,

                          CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                          CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE likes (
                       post_id UUID REFERENCES posts(id),
                       user_id UUID NOT NULL,
                       PRIMARY KEY (post_id, user_id),

                       CONSTRAINT fk_likes_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
                       CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE subscriptions (
                               follower_id UUID NOT NULL,
                               following_id UUID NOT NULL,
                               PRIMARY KEY (follower_id, following_id),

                               CONSTRAINT fk_follower FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
                               CONSTRAINT fk_following FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,

                               CONSTRAINT check_not_self_subscribe CHECK (follower_id <> following_id)
);
