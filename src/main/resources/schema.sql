CREATE TABLE IF NOT EXISTS users (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		email VARCHAR NOT NULL,
		login VARCHAR NOT NULL,
		name VARCHAR,
		birthday DATE);

CREATE TABLE IF NOT EXISTS mpa_rating (
        id INTEGER PRIMARY KEY,
		name VARCHAR NOT NULL,
		description VARCHAR NOT NULL);

CREATE TABLE IF NOT EXISTS movies (
        id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
		release_date DATE NOT NULL,
		name VARCHAR NOT NULL,
		description VARCHAR(200),
		mpa_rating_id INTEGER REFERENCES mpa_rating (id) ON DELETE RESTRICT,
		duration INTEGER NOT NULL);

CREATE TABLE IF NOT EXISTS genres (
        id INTEGER PRIMARY KEY,
		name varchar);

CREATE TABLE IF NOT EXISTS genre_movie (
        movie_id INTEGER REFERENCES movies (id) ON DELETE CASCADE,
		genre_id INTEGER REFERENCES genres (id) ON DELETE RESTRICT);

CREATE TABLE IF NOT EXISTS likedMovies (
       user_who_liked_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
	   movie_id INTEGER REFERENCES movies (id) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS friendship (
       requester_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
	   requestee_id INTEGER REFERENCES users (id) ON DELETE CASCADE,
	   friendship_status varchar,
	   CONSTRAINT chk_Friendship CHECK (friendship_status IN ('Confirmed', 'Not confirmed')));