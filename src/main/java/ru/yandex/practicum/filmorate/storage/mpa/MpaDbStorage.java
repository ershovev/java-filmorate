package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Primary
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "SELECT * FROM mpa_rating;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa); // вызываем mapRowToMpa и преобразуем строки в объекты MPA
    }

    @Override
    public Mpa getMpa(Integer id) {
        String sqlQuery = "SELECT * FROM mpa_rating WHERE id = ?;";

        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id); //отправляем запрос и получаем MPA
    }

    @Override
    public boolean isMpaPresentById(Integer id) {
        String sqlQuery = "SELECT id " +
                "FROM mpa_rating WHERE id = ?;";
        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{id}, (ResultSet rs) -> {
            return rs.next();
        }); // проверяем есть ли строка в ответе

        return exists;
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {  //преобразовать строку в объект Mpa
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
