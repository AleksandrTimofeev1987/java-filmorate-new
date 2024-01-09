DROP TABLE IF EXISTS films CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS film_likes CASCADE;
DROP TABLE IF EXISTS mpa CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS film_genre CASCADE;
DROP TABLE IF EXISTS user_friends CASCADE;

CREATE TABLE users
(
    user_id  int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email    varchar(40)                                  NOT NULL,
    login    varchar(40)                                  NOT NULL,
    user_name     varchar(40)                                  NOT NULL,
    birthday date                                         NOT NULL
);

CREATE TABLE mpa
(
    mpa_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    rating varchar(40)                                  NOT NULL
);

CREATE TABLE films
(
    film_id      int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name         varchar(40)                                  NOT NULL,
    description  varchar(200)                                 NOT NULL,
    release_date date                                         NOT NULL,
    duration     int                                          NOT NULL,
    rate         int                                          NOT NULL,
    mpa_id       int                                          NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id)
);

CREATE TABLE genres
(
    genre_id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    genre    varchar(40)                                  NOT NULL
);

CREATE TABLE film_genre
(
    film_id  int NOT NULL,
    genre_id int NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE film_likes
(
    film_id int NOT NULL,
    user_id int NOT NULL,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    PRIMARY KEY (film_id, user_id)
);

CREATE TABLE user_friends
(
    user_id int NOT NULL,
    friend_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id),
    PRIMARY KEY (user_id, friend_id)
);

