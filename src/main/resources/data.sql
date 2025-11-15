-- Voeg rollen toe als ze nog niet bestaan
INSERT INTO roles (name)
SELECT 'ADMIN'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name)
SELECT 'QUIZTAKER'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'QUIZTAKER');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Inception', 'Sci-Fi', 148, 2010, 'Netflix'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Inception');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'The Dark Knight', 'Action', 152, 2008, 'HBO Max'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'The Dark Knight');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Interstellar', 'Adventure', 169, 2014, 'Amazon Prime'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Interstellar');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Forrest Gump', 'Drama', 142, 1994, 'Netflix'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Forrest Gump');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'The Matrix', 'Sci-Fi', 136, 1999, 'HBO Max'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'The Matrix');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'The Godfather', 'Crime', 175, 1972, 'Amazon Prime'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'The Godfather');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Pulp Fiction', 'Thriller', 154, 1994, 'Netflix'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Pulp Fiction');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Spider-Man: Into the Spider-Verse', 'Animation', 117, 2018, 'Amazon Prime'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Spider-Man: Into the Spider-Verse');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'The Lion King', 'Family', 88, 1994, 'HBO Max'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'The Lion King');

INSERT INTO movies (title, genre, length_in_minutes, release_year, streaming_service)
SELECT 'Gladiator', 'War', 155, 2000, 'Netflix'
    WHERE NOT EXISTS (SELECT 1 FROM movies WHERE title = 'Gladiator');
