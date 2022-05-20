PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE certificado (id bigint not null, clave_publica varchar, primary key (id));
CREATE TABLE hibernate_sequence (next_val bigint);
INSERT INTO hibernate_sequence VALUES(12);
INSERT INTO hibernate_sequence VALUES(12);
INSERT INTO hibernate_sequence VALUES(12);
INSERT INTO hibernate_sequence VALUES(12);
DROP TABLE peticion;
CREATE TABLE peticion (id bigint not null, camas integer, mesas integer,  sillas integer, sillones integer, timestamp timestamp, verificacion integer, nonce varchar, primary key (id));
INSERT INTO peticion VALUES(1,10,15,5,20,'2020-11-15 15:30:14.332',1,'274911e2-cb4f-41fe-819b-c211832e5452');
INSERT INTO peticion VALUES(2,5,125,0,200,'2020-11-20 15:30:14.332',0,'274911e2-cb4f-41fe-819b-c211832e5453');
INSERT INTO peticion VALUES(3,1,5,5,0,'2020-12-01 15:30:14.332',1,'274911e2-cb4f-41fe-819b-c211832e5454');
INSERT INTO peticion VALUES(4,0,0,12,20,'2020-12-14 15:30:14.332',0,'274911e2-cb4f-41fe-819b-c211832e5455');
INSERT INTO peticion VALUES(5,100,15,4,20,'2020-12-31 15:30:14.332',1,'274911e2-cb4f-41fe-819b-c211832e5456');
INSERT INTO peticion VALUES(6,10,15,5,20,'2021-11-15 15:30:14.332',1,'274911e2-cb4f-41fe-819b-c211832e5452');
INSERT INTO peticion VALUES(7,5,125,0,200,'2021-11-20 15:30:14.332',1,'374911e2-cb4f-41fe-819b-c211832e5453');
INSERT INTO peticion VALUES(8,1,5,5,0,'2021-12-01 15:30:14.332',0,'474911e2-cb4f-41fe-819b-c211832e5454');
INSERT INTO peticion VALUES(9,0,0,12,20,'2021-12-14 15:30:14.332',1,'574911e2-cb4f-41fe-819b-c211832e5455');
INSERT INTO peticion VALUES(10,100,15,4,20,'2021-12-31 15:30:14.332',1,'674911e2-cb4f-41fe-819b-c211832e5456');
INSERT INTO peticion VALUES(11,10,15,5,20,'2022-03-15 15:30:14.332',0,'014911e2-cb4f-41fe-819b-c211832e5452');
INSERT INTO peticion VALUES(12,5,125,0,200,'2022-03-20 15:30:14.332',1,'024911e2-cb4f-41fe-819b-c211832e5453');
INSERT INTO peticion VALUES(13,1,5,5,0,'2022-04-01 15:30:14.332',1,'034911e2-cb4f-41fe-819b-c211832e5454');
INSERT INTO peticion VALUES(14,0,0,12,20,'2022-04-14 15:30:14.332',1,'044911e2-cb4f-41fe-819b-c211832e5455');
INSERT INTO peticion VALUES(15,100,15,4,20,'2022-04-31 15:30:14.332',0,'054911e2-cb4f-41fe-819b-c211832e5456');
INSERT INTO peticion VALUES(16,10,15,5,20,'2022-05-01 15:30:14.332',0,'064911e2-cb4f-41fe-819b-c211832e5452');
INSERT INTO peticion VALUES(17,5,125,0,200,'2022-05-10 15:30:14.332',1,'074911e2-cb4f-41fe-819b-c211832e5453');
INSERT INTO peticion VALUES(18,1,5,5,0,'2022-05-15 15:30:14.332',1,'084911e2-cb4f-41fe-819b-c211832e5454');
INSERT INTO peticion VALUES(19,0,0,12,20,'2022-05-19 15:30:14.332',1,'094911e2-cb4f-41fe-819b-c211832e5455');
COMMIT;
