# --- !Ups

CREATE TABLE PHRASE (
    ID bigint(20) NOT NULL AUTO_INCREMENT,
    SPANISH varchar(255) NOT NULL,
    ENGLISH varchar(255) NOT NULL,
    --CREATEDAT datetime NOT NULL,
    PRIMARY KEY (id)
);

create table "CAT" ("NAME" VARCHAR NOT NULL PRIMARY KEY,"COLOR" VARCHAR NOT NULL);


# --- !Downs

DROP TABLE PHRASE;

drop table "CAT";
