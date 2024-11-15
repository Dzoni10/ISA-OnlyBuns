INSERT INTO "app_user" (name, surname, username, password, email, adress, login_status, is_admin, last_activity)
VALUES
    ('Administrator', 'Administratorovic', 'admin', 'admin', 'admin@gmail', 'Ulica122000', TRUE, TRUE, '2024-11-01T10:00:00'),
    ('Pera', 'Peric', 'pera123', '123', 'pera@gmail', 'Ulica122000', FALSE, FALSE, '2024-11-14T10:00:00'),
    ('Mika', 'Mikic', 'mika123', '123', 'mika@gmail', 'Ulica122000', TRUE, FALSE, '2024-10-31T10:00:00'),
    ('Laza', 'Lazic', 'laza123', '123', 'djordjer88@gmail.com', 'Ulica122000', TRUE, FALSE, '2024-11-02T10:00:00'),
    ('Djura', 'Djuric', 'djura123', '123', 'djura@gmail', 'Ulica122000', TRUE, FALSE, '2024-11-05T10:00:00'),
    ('Aca', 'Acic', 'aca123', '123', 'aca@gmail', 'Ulica122000', FALSE, FALSE, '2024-11-15T10:00:00'),
    ('Mila', 'Milic', 'mila123', '123', 'mila@gmail', 'Ulica122000', TRUE, FALSE, '2024-11-02T10:00:00'),
    ('Ljuba', 'Ljubic', 'ljuba123', '123', 'ljuba@gmail', 'Ulica122000', TRUE, FALSE, '2024-10-25T10:00:00');

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path, username)
VALUES (4, 'Obican mali zec, spreman za veceru.', '2024-11-10 09:00:00', 0, 0, '','laza123' );

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path, username)
VALUES (3, 'Milivojev zec, jedini preziveli.', '2024-11-10 11:30:00',0,0, '','mika123');

INSERT INTO "app_comment" (post_id, username, creation_time, text)
VALUES
    (2, 'laza123', '2024-11-10 11:45:00', 'Zec je stvarno brz, kao da je trenirao za Olimpijadu!'),
    (2, 'mika123', '2024-11-10 12:00:00', 'Zec je brz kao munja, moram da ga zovem za trkača!'),
    (2, 'pera123', '2024-11-10 12:15:00', 'Zec je prava zver kad se trči! Sigurno je iz najbržeg plemena.'),
    (1, 'laza123', '2024-11-10 12:15:00', 'Zec je simpatičan, ali da je još brži, sigurno bi pobegao od svih predatora!'),
    (1, 'pera123', '2024-11-10 12:15:00', 'Zec je kao raketa, ali bi mogao da se opusti i uživa u prirodi.'),
    (1, 'zika123', '2024-11-10 12:15:00', 'Zec je pravi majstor za preživljavanje, brzo beži kad nešto čuje.'),
    (1, 'mika123', '2024-11-10 12:15:00', 'Zec je brz i mudar, samo što ponekad može da postane previše oprezan!');
