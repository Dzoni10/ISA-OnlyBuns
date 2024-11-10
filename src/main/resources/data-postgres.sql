insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Pera','Peric','pera123','123','pera@gmail','Ulica122000',FALSE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Mika','Mikic','mika123','123','mika@gmail','Ulica122000',TRUE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Laza','Lazic','laza123','123','laza@gmail','Ulica122000',TRUE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Djura','Djuric','djura123','123','djura@gmail','Ulica122000',TRUE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Aca','Acic','aca123','123','aca@gmail','Ulica122000',FALSE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Mila','Milic','mila123','123','mila@gmail','Ulica122000',TRUE);
insert into "app_user" (name,surname,username,password,email,adress,login_status) values ('Ljuba','Ljubic','ljuba123','123','ljuba@gmail','Ulica122000',TRUE);

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path)
VALUES (4, 'Obican mali zec, spreman za veceru.', '2024-11-10 09:00:00', 0, 0, '' );

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path)
VALUES (6, 'Risticev zec, mnogo sladak i mekan.', '2024-11-10 10:15:00', 0 ,0, '');

INSERT INTO "app_post" (creator_id, description, creation_time, latitude, longitude, image_path)
VALUES (3, 'Milivojev zec, jedini preziveli.', '2024-11-10 11:30:00',0,0, '');

INSERT INTO "app_comment" (post_id, username, creation_time, text)
VALUES
    (3, 'laza123', '2024-11-10 11:45:00', 'Zec je stvarno brz, kao da je trenirao za Olimpijadu!'),
    (3, 'mika123', '2024-11-10 12:00:00', 'Zec je brz kao munja, moram da ga zovem za trkača!'),
    (3, 'pera123', '2024-11-10 12:15:00', 'Zec je prava zver kad se trči! Sigurno je iz najbržeg plemena.'),
    (2, 'laza123', '2024-11-10 12:15:00', 'Zec je simpatičan, ali da je još brži, sigurno bi pobegao od svih predatora!'),
    (2, 'pera123', '2024-11-10 12:15:00', 'Zec je kao raketa, ali bi mogao da se opusti i uživa u prirodi.'),
    (1, 'zika123', '2024-11-10 12:15:00', 'Zec je pravi majstor za preživljavanje, brzo beži kad nešto čuje.'),
    (1, 'mika123', '2024-11-10 12:15:00', 'Zec je brz i mudar, samo što ponekad može da postane previše oprezan!');
