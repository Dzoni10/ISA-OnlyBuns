insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Administrator','Administratorovic','admin','admin','admin@gmail','Bulevar Mihajla Pupina 165, Novi Beograd',TRUE,TRUE,0);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Pera','Peric','pera123','123','pera@gmail','Kralja Petra I 23, Novi Sad',FALSE,FALSE,0);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Mika','Mikic','mika123','123','mika@gmail','Njegoševa 14, Niš',TRUE,FALSE,1);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Laza','Lazic','laza123','123','laza@gmail','Cara Dušana 89, Kragujevac',TRUE,FALSE,1);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Djura','Djuric','djura123','123','djura@gmail','Zmaj Jovina 4, Subotica',TRUE,FALSE,0);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Aca','Acic','aca123','123','aca@gmail','Svetozara Miletića 28, Zrenjanin',FALSE,FALSE,0);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Mila','Milic','mila123','123','mila@gmail','Karađorđeva 103, Pančevo',TRUE,FALSE,0);
insert into "app_user" (name,surname,username,password,email,adress,login_status,is_admin,post_count) values ('Ljuba','Ljubic','ljuba123','123','ljuba@gmail','Sinđelićeva 55, Čačak',TRUE,FALSE,0);

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