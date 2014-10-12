CREATE TABLE users      (id       int NOT NULL AUTO_INCREMENT,
                         email    varchar(64),
                         password varchar(64),
                         PRIMARY KEY(id));

CREATE TABLE feeds      (id          int NOT NULL AUTO_INCREMENT,
                         title       varchar(128),
                         url         varchar(256),
                         link        varchar(256),
                         description text, 
                         favicon     varchar(256),
                         updated_at  date,
                         PRIMARY KEY(id));

CREATE TABLE feed_posts (id               int NOT NULL AUTO_INCREMENT,
                         slug             varchar(100),
                         title            varchar(256),
                         category         varchar(256),
                         comments         varchar(256),
                         link             varchar(256),
                         description      text,
                         publication_date date,
                         source           text,
                         PRIMARY KEY(id));


CREATE TABLE user_feeds (user_id int,
                         feed_id int);
