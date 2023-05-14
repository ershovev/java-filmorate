INSERT INTO MPA_RATING (id, name, description)
SELECT * FROM (SELECT 1 AS id, 'G' AS name, 'У фильма нет возрастных ограничений' AS description) AS temp
WHERE NOT EXISTS (SELECT id FROM MPA_RATING
                  WHERE id = 1);

INSERT INTO MPA_RATING (id, name, description)
SELECT * FROM (SELECT 2 AS id, 'PG' AS name, 'Детям рекомендуется смотреть фильм с родителями' AS description) AS temp
WHERE NOT EXISTS (SELECT id FROM MPA_RATING
                  WHERE id = 2);

INSERT INTO MPA_RATING (id, name, description)
SELECT * FROM (SELECT 3 AS id, 'PG-13' AS name, 'Детям до 13 лет просмотр не желателен' AS description) AS temp
WHERE NOT EXISTS (SELECT id FROM MPA_RATING
                  WHERE id = 3);

INSERT INTO MPA_RATING (id, name, description)
SELECT * FROM (SELECT 4 AS id, 'R' AS name, 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого' AS description) AS temp
WHERE NOT EXISTS (SELECT id FROM MPA_RATING
                  WHERE id = 4);

INSERT INTO MPA_RATING (id, name, description)
SELECT * FROM (SELECT 5 AS id, 'NC-17' AS name, 'Лицам до 18 лет просмотр запрещён' AS description) AS temp
WHERE NOT EXISTS (SELECT id FROM MPA_RATING
                  WHERE id = 5);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 1 AS id, 'Комедия' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 1);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 2 AS id, 'Драма' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 2);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 3 AS id, 'Мультфильм' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 3);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 4 AS id, 'Триллер' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 4);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 5 AS id, 'Документальный' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 5);

INSERT INTO GENRES (id, name)
SELECT * FROM (SELECT 6 AS id, 'Боевик' AS name) AS temp
WHERE NOT EXISTS (SELECT id FROM GENRES
                  WHERE id = 6);