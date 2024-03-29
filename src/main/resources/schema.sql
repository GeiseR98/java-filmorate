DROP TABLE IF EXISTS users, films, MPA, genre, film_genre, film_likes, friends CASCADE;

CREATE TABLE IF NOT EXISTS MPA (
    mpa_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    mpa_name VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(60) NOT NULL,
    email VARCHAR(254) NOT NULL, --https://stackoverflow.com/questions/386294/what-is-the-maximum-length-of-a-valid-email-address
    login VARCHAR(36) NOT NULL,
    birthday DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1500) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(60) NOT NULL
    );

CREATE TABLE IF NOT EXISTS likes (
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS friends (
    friend_one_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friend_two_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    status boolean
    );

CREATE TABLE IF NOT EXISTS film_genre (
    genre_id INTEGER REFERENCES genre (genre_id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, genre_id)
    );

CREATE TABLE IF NOT EXISTS film_mpa (
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    mpa_id INTEGER REFERENCES mpa (mpa_id) ON DELETE CASCADE,
    PRIMARY KEY (film_id, mpa_id)
    );