# java-filmorate
Template repository for Filmorate project.

DB scheme

![DB scheme](src/main/resources/db_scheme.png)

Query examples:

1) Get all friends
```
SELECT *
FROM users
WHERE id IN (SELECT requester_id
             FROM friendship
             WHERE requestee_id IS :user_id
             UNION
             SELECT requestee_id
             FROM friendship
             WHERE requester_id IS :user_id AND status = 'Confirmed');
```

2) Get common friends
```
SELECT *
FROM users
WHERE id IN (SELECT requester_id
             FROM friendship
             WHERE requestee_id IS :user_id
             UNION
             SELECT requestee_id
             FROM friendship
             WHERE requester_id IS :user_id AND status = 'Confirmed')
AND id IN (SELECT requester_id
             FROM friendship
             WHERE requestee_id IS :another_user_id
             UNION
             SELECT requestee_id
             FROM friendship
             WHERE requester_id IS :another_user_id AND status = 'Confirmed');
```
3) Get most popular films
```
SELECT id, release_date, name, description, mpa_rating_id, duration
FROM movies AS mov
JOIN (SELECT movie_id, count(movie_id) AS likes_count
                   FROM likedMovies
                   GROUP BY movie_id
                   ORDER BY likes_count DESC
                   LIMIT :limit) AS toplikes ON mov.id = toplikes.movie_id
ORDER BY toplikes.likes_count DESC;
```