package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("movies")    // insert в таблицу БД movies
                .usingGeneratedKeyColumns("id");  // получаем сгенерированный БД id фильма
        Integer filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).intValue(); // сохраняем фильм в БД и получаем id фильма
        film.setId(filmId);

        if (film.getGenres() != null) {
            insertFilmAndGenreIdIntoDb(film); // вызываем метод для создания записей в таблице БД genre_movie
        } else {
            film.setGenres(new ArrayList<>());
        }

        return getFilm(filmId);
    }

    @Override
    public Film update(Film film) {
        deleteFilmAndGenreIdFromDb(film); // вызываем метод для удаления записей в таблице БД genre_movie
        String sqlQuery = "UPDATE movies SET release_date = ?, name = ?, description = ?," +
                " mpa_rating_id = ?, duration = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getReleaseDate(),
                film.getName(),
                film.getDescription(),
                film.getMpa().getId(),
                film.getDuration(),
                film.getId());

        if (film.getGenres() != null) {
            insertFilmAndGenreIdIntoDb(film); // вызываем метод для создания записей в таблице БД genre_movie
        } else {
            film.setGenres(new ArrayList<>());
        }
        return getFilm(film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM movies AS film " +
                "LEFT JOIN mpa_rating AS mpa ON film.mpa_rating_id = mpa.id;";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm); // вызываем mapRowToFilm и преобразуем строки в объекты Film
        for (Film film : films) {
            film.setGenres(getGenresOfFilm(film.getId())); // добавляем жанры
        }
        return films;
    }

    @Override
    public boolean isFilmPresent(Film film) {
        return isFilmPresentById(film.getId());
    }

    @Override
    public boolean isFilmPresentById(Integer id) {
        String sqlQuery = "SELECT id " +
                "FROM movies WHERE id = ?;";
        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{id}, (ResultSet rs) -> {
            return rs.next();
        }); // проверяем есть ли строка в ответе

        return exists;
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "SELECT * FROM movies AS film " +
                "LEFT JOIN mpa_rating AS mpa ON film.mpa_rating_id = mpa.id " +
                "WHERE film.id = ?;";

        Film film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id); //отправляем запрос и получаем фильм
        film.setGenres(getGenresOfFilm(id)); // добавляем жанры

        return film;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "MERGE INTO likedmovies (user_who_liked_id, movie_id)" +
                " KEY (user_who_liked_id, movie_id) VALUES (?, ?);";

        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM likedmovies WHERE movie_id = ? AND user_who_liked_id = ?;";

        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(Integer size) {
        String sqlQuery = "SELECT m.id, m.release_date, m.name, m.description, m.duration, mr.id, mr.name, mr.description" +
                " FROM movies AS m" +
                " LEFT JOIN likedMovies AS lm ON m.id = lm.movie_id" +
                " LEFT JOIN mpa_rating AS mr ON m.mpa_rating_id = mr.id" +
                " GROUP BY m.id" +
                " ORDER BY COUNT(lm.user_who_liked_id) DESC" +
                " LIMIT ?;";

        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, size); //отправляем запрос и получаем фильмы

        for (Film film : films) {
            film.setGenres(getGenresOfFilm(film.getId()));  // добавляем жанры
        }

        return films;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {  //преобразовать строку в объект Film
        Film film = Film.builder()
                .id(resultSet.getInt("id"))
                .releaseDate(LocalDate.parse(resultSet.getString("release_date")))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .mpa(Mpa.builder()                                                //билдим объект Mpa
                        .id(resultSet.getInt("mpa_rating.id"))
                        .name(resultSet.getString("mpa_rating.name"))
                        .description(resultSet.getString("mpa_rating.description"))
                        .build())
                .duration(resultSet.getInt("duration"))
                .build();

        if (film.getGenres() != null) {
            insertFilmAndGenreIdIntoDb(film); // вызываем метод для создания записей в таблице БД genre_movie
        } else {
            film.setGenres(new ArrayList<>());
        }

        return film;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException { //преобразовать строку в объект Genre
        return Genre.builder()
                .id(resultSet.getInt("genres.id"))
                .name(resultSet.getString("genres.name"))
                .build();
    }

    private List<Genre> getGenresOfFilm(Integer id) {          // получить все жанры фильма
        String sqlQuery = "SELECT g.id, g.name FROM genre_movie AS gm " +
                "JOIN genres AS g on gm.GENRE_ID = g.id " +
                "WHERE movie_id = ?;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);   //вызываем mapRowToGenre чтобы преобразовать
        //каждую строку в объект Genre
    }

    private void insertFilmAndGenreIdIntoDb(Film film) {
        String sqlQuery;
        List<Integer> filmIdGenreId = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        for (Genre genre : film.getGenres()) {    // проходим по жанрам и добавляем запросы в stringbuilder и id фильмов и жанров в arraylist
            stringBuilder.append("MERGE INTO GENRE_MOVIE (movie_id, genre_id) KEY (movie_id, genre_id) VALUES (?, ?); ");
            filmIdGenreId.add(film.getId());
            filmIdGenreId.add(genre.getId());
        }
        sqlQuery = stringBuilder.toString();
        jdbcTemplate.update(sqlQuery, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                for (int i = 1; i <= filmIdGenreId.size(); i++) {  // добавляем id фильмов и жанров в запрос
                    ps.setInt(i, filmIdGenreId.get(i - 1));
                }
            }
        });
    }

    private void deleteFilmAndGenreIdFromDb(Film film) {
        String sqlQuery = "DELETE FROM genre_movie WHERE movie_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId());
    }
}
