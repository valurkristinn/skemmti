INSERT INTO users (user_id, username, password, image, description, role) VALUES
    (1, 'admin', '$2a$10$8ePt.GuVMAnYr3C/RdrXuu2OGosscdanOUZWgMFaHHzU2F32bRy5.', '', 'admin', 'ADMIN'),
    (2, 'Oktoberfest', '$2a$10$8ePt.GuVMAnYr3C/RdrXuu2OGosscdanOUZWgMFaHHzU2F32bRy5.', '', 'Októberfest Háskóla Íslands', 'ORGANIZER'),
    (3, 'Jon Jonsson', '$2a$10$8ePt.GuVMAnYr3C/RdrXuu2OGosscdanOUZWgMFaHHzU2F32bRy5.', '', 'Venjulegur náungi', 'MEMBER');

INSERT INTO event (event_id, name, date, start_time, location, description, tag, image, organizer_user_id, published) VALUES
    (1, 'Októberfest dagur 1', '2026-09-10', '17:00:00', 'Háskóli Íslands', 'Októberfest!', 'Októberfest', '', 2, true),
    (2, 'Októberfest dagur 2', '2026-09-11', '16:00:00', 'Háskóli Íslands', 'Októberfest!', '', '', 2, true),
    (3, 'Októberfest dagur 3', '2026-09-12', '16:00:00', 'Háskóli Íslands', 'Októberfest!', '', '', 2, false);

INSERT INTO users_attending_events (user_user_id, attending_events_event_id) VALUES
    (3, 1),
    (3, 2);

INSERT INTO users_favorite_organizers (user_user_id, favorite_organizers_user_id) VALUES
    (3, 2);

ALTER TABLE users ALTER COLUMN user_id RESTART WITH 100;
ALTER TABLE event ALTER COLUMN event_id RESTART WITH 100;
