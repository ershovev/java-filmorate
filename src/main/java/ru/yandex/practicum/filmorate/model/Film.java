package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class Film {
    private Integer id;
    private List<Genre> genres;
    private Mpa mpa;
    private LocalDate releaseDate;
    @NotBlank
    private final String name;
    @Size(min = 0, max = 200)
    private final String description;
    @Positive
    private final int duration;

    public Map<String, Object> toMap() {  // метод создания хэшмапы с данными фильма
        Map<String, Object> values = new HashMap<>();  // для использования в simpleJdbcInsert
        values.put("release_date", releaseDate);
        values.put("mpa_rating_id", mpa.getId());
        values.put("name", name);
        values.put("description", description);
        values.put("duration", duration);
        return values;
    }
}