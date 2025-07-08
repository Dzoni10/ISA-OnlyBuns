
INSERT INTO "app_user" (name, surname, username, password, email, login_status, is_admin,post_count, last_activity,latitude,longitude)
VALUES
    ('Administrator', 'Administratorovic', 'admin', 'admin', 'admin@gmail', TRUE, TRUE, 0, '2024-11-01T10:00:00', 44.787052, 20.4828698),
    ('Pera', 'Peric', 'pera123', '123', 'pera@gmail', TRUE, FALSE, 0, '2024-11-14T10:00:00', 44.8167, 20.4569),
    ('Mika', 'Mikic', 'mika123', '123', 'mika@gmail', TRUE, FALSE, 1, '2024-10-31T10:00:00', 43.3209, 21.8958),
    ('Laza', 'Lazic', 'laza123', '123', 'djordjer88@gmail.com', TRUE, FALSE, 1, '2024-11-02T10:00:00', 44.0165, 21.0059),
    ('Djura', 'Djuric', 'djura123', '123', 'djura@gmail', TRUE, FALSE, 0, '2024-11-05T10:00:00', 43.8600, 20.2976),
    ('Aca', 'Acic', 'aca123', '123', 'aca@gmail', TRUE, FALSE, 0, '2024-11-15T10:00:00', 44.6673, 21.1881),
    ('Mila', 'Milic', 'mila123', '123', 'mila@gmail', TRUE, FALSE, 0, '2024-11-02T10:00:00', 43.2535, 22.0059),
    ('Ljuba', 'Ljubic', 'ljuba123', '123', 'ljuba@gmail', TRUE, FALSE, 0, '2024-10-25T10:00:00', 45.2517, 19.8369);


INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path, username)
VALUES (4, 'Obican mali zec, spreman za veceru.', '2024-11-10 09:00:00', 44.824013, 20.459031, '','laza123' );

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path, username)
VALUES (3, 'Milivojev zec, jedini preziveli.', '2024-11-10 11:30:00',44.819104, 20.459110,'', 'mika123');

INSERT INTO "app_comment" (post_id, username, creation_time, text)
VALUES
    (2, 'laza123', '2024-11-10 11:45:00', 'Zec je stvarno brz, kao da je trenirao za Olimpijadu!'),
    (2, 'mika123', '2024-11-10 12:00:00', 'Zec je brz kao munja, moram da ga zovem za trkača!'),
    (2, 'pera123', '2024-11-10 12:15:00', 'Zec je prava zver kad se trči! Sigurno je iz najbržeg plemena.'),
    (1, 'laza123', '2024-11-10 12:15:00', 'Zec je simpatičan, ali da je još brži, sigurno bi pobegao od svih predatora!'),
    (1, 'pera123', '2024-11-10 12:15:00', 'Zec je kao raketa, ali bi mogao da se opusti i uživa u prirodi.'),
    (1, 'zika123', '2024-11-10 12:15:00', 'Zec je pravi majstor za preživljavanje, brzo beži kad nešto čuje.'),
    (1, 'mika123', '2024-11-10 12:15:00', 'Zec je brz i mudar, samo što ponekad može da postane previše oprezan!');
INSERT INTO "following" (username, following, creation_time)
VALUES
    ('mika123', 'laza123', '2024-11-10 11:45:00'),
    ('mika123', 'pera123', '2024-11-10 11:45:00'),
    ('laza123', 'pera123', '2024-11-10 12:15:00');
INSERT INTO app_like (post_id, username)
VALUES
    (1, 'mika123'),
    (1, 'laza123'),
    (2, 'pera123'),
    (1, 'pera123');
INSERT INTO app_chat (name, admin_username)
VALUES
    ( 'Java Enthusiasts', 'mika123'),
    ( 'Vue.js Learners', 'laza123'),
    ( 'Travel Buddies', 'pera123');

INSERT INTO chat_participants (chat_id, username, join_time)
VALUES
    (1, 'mika123', '2025-02-14 10:30:00'),
    (1, 'pera123', '2025-02-14 10:30:00'),
    (1, 'laza123', '2025-02-14 10:30:00'),
    (2, 'laza123', '2025-02-14 10:30:00'),
    (2, 'mika123', '2025-02-14 10:30:00'),
    (3, 'pera123', '2025-02-14 10:30:00'),
    (3, 'laza123', '2025-02-14 10:30:00');

INSERT INTO app_chat_message ( username, text, creation_time, chat_id)
VALUES
    ('mika123', 'Welcome to the Java group!', '2024-11-10 10:00:00', 1),
    ('pera123', 'Excited to learn with everyone!', '2024-11-10 10:05:00', 1),
    ('laza123', 'This chat is great for Java discussions!', '2024-11-10 10:10:00', 1),
    ('laza123', 'Hello Vue.js learners!', '2024-11-10 11:00:00', 2),
    ('mika123', 'Let’s start with Vue basics.', '2024-11-10 11:10:00', 2),
    ('pera123', 'Traveling is so much fun!', '2024-11-10 12:00:00', 3),
    ('laza123', 'Agreed! Let’s plan our next trip.', '2024-11-10 12:15:00', 3),
    ('mika123', 'Has anyone tried Java 21 features?', '2024-11-10 10:20:00', 1),
    ('pera123', 'Yes! The virtual threads are amazing.', '2024-11-10 10:25:00', 1),
    ('laza123', 'What’s the best way to learn Vue 3?', '2024-11-10 11:20:00', 2),
    ( 'mika123', 'Start with the official docs, they are great.', '2024-11-10 11:25:00', 2),
    ( 'pera123', 'We should create a travel bucket list!', '2024-11-10 12:30:00', 3),
    ( 'laza123', 'I’d love to visit Japan next year.', '2024-11-10 12:45:00', 3),
    ( 'mika123', 'Japan sounds awesome! Let’s do it.', '2024-11-10 12:50:00', 3),
    ( 'pera123', 'What’s your favorite programming language?', '2024-11-10 13:00:00', 1),
    ( 'laza123', 'I love JavaScript and Vue.js!', '2024-11-10 13:05:00', 2),
    ( 'mika123', 'Python is my go-to for data science.', '2024-11-10 13:10:00', 1),
    ( 'pera123', 'Anyone up for a weekend trip?', '2024-11-10 14:00:00', 3),
    ( 'laza123', 'Yes, let’s go hiking!', '2024-11-10 14:15:00', 3),
    ( 'mika123', 'Vue 3 Composition API is powerful!', '2024-11-10 14:30:00', 2),
    ( 'pera123', 'I still prefer Options API for beginners.', '2024-11-10 14:45:00', 2);
