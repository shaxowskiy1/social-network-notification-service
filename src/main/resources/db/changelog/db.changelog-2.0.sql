--liquibase formatted sql

--changeset SPavelin:1
INSERT INTO users (username, password, first_name, last_name, is_active, roles) VALUES
                                                                                    ('seryozha', 'password123', 'Серёжа', 'Иванов', true, ARRAY['USER']),
                                                                                    ('sveta', 'securepass456', 'Света', 'Петрова', true, ARRAY['USER']);

INSERT INTO posts (id, user_id, content, file_id, created_at) VALUES
                                                                  ('550e8400-e29b-41d4-a716-446655440000', (SELECT id FROM users WHERE username = 'seryozha'),
                                                                   'Мой первый пост! Как вам?', 'file_001.jpg', '2026-01-01 10:00:00'),
                                                                  ('550e8400-e29b-41d4-a716-446655440001', (SELECT id FROM users WHERE username = 'seryozha'),
                                                                   'Отличная погода сегодня!', 'file_002.jpg', '2026-01-02 14:30:00'),
                                                                  ('550e8400-e29b-41d4-a716-446655440002', (SELECT id FROM users WHERE username = 'sveta'),
                                                                   'Новый рецепт пирога!', NULL, '2026-01-03 18:45:00'),
                                                                  ('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM users WHERE username = 'sveta'),
                                                                   'Вчерашний закат был прекрасен', 'file_003.jpg', '2026-01-04 09:15:00');

INSERT INTO comments (id, post_id, user_id, content, created_at) VALUES
                                                                     ('650e8400-e29b-41d4-a716-446655440000', '550e8400-e29b-41d4-a716-446655440000',
                                                                      (SELECT id FROM users WHERE username = 'sveta'), 'Классный пост!', '2026-01-01 10:30:00'),
                                                                     ('650e8400-e29b-41d4-a716-446655440001', '550e8400-e29b-41d4-a716-446655440000',
                                                                      (SELECT id FROM users WHERE username = 'seryozha'), 'Спасибо!', '2026-01-01 10:35:00'),
                                                                     ('650e8400-e29b-41d4-a716-446655440002', '550e8400-e29b-41d4-a716-446655440002',
                                                                      (SELECT id FROM users WHERE username = 'seryozha'), 'Обязательно попробую!', '2026-01-03 19:00:00'),
                                                                     ('650e8400-e29b-41d4-a716-446655440003', '550e8400-e29b-41d4-a716-446655440003',
                                                                      (SELECT id FROM users WHERE username = 'seryozha'), 'Красивый закат!', '2026-01-04 09:30:00');

INSERT INTO likes (post_id, user_id) VALUES
                                         ('550e8400-e29b-41d4-a716-446655440000', (SELECT id FROM users WHERE username = 'sveta')),
                                         ('550e8400-e29b-41d4-a716-446655440002', (SELECT id FROM users WHERE username = 'seryozha')),
                                         ('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM users WHERE username = 'seryozha')),
                                         ('550e8400-e29b-41d4-a716-446655440003', (SELECT id FROM users WHERE username = 'sveta'));

INSERT INTO subscriptions (follower_id, following_id) VALUES
    ((SELECT id FROM users WHERE username = 'seryozha'), (SELECT id FROM users WHERE username = 'sveta'));

INSERT INTO users (username, password, first_name, last_name, is_active, roles) VALUES
    ('admin', 'adminpass789', 'Админ', 'Системный', true, ARRAY['USER', 'ADMIN']);
