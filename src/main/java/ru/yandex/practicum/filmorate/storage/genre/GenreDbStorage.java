package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        String sqlQuery = "SELECT * FROM genres;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre); // вызываем mapRowToGenre и преобразуем строки в объекты Genre
    }

    @Override
    public Genre getGenre(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?;";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id); //отправляем запрос и получаем Genre
    }

    @Override
    public boolean isGenrePresentById(Integer id) {
        String sqlQuery = "SELECT id " +
                "FROM genres WHERE id = ?;";
        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{id}, (ResultSet rs) -> {
            return rs.next();
        }); // проверяем есть ли строка в ответе
        return exists;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {  //преобразовать строку в объект Genre
        Genre genre = Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();

        return genre;
    }
}
