package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmIdCounter = 1;
    private static final LocalDate BIRTHDAY_OF_CINEMA = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(BIRTHDAY_OF_CINEMA)) {
            log.debug("Ошибка добавления фильма: Дата выхода фильма раньше дня рождения кино");
            throw new ValidationException("Дата выхода фильма раньше дня рождения кино");
        }
        film.setId(filmIdCounter);
        films.put(filmIdCounter, film);
        filmIdCounter++;
        log.debug("Добавлен фильм: {}", film.toString());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws NoSuchFilmException {
        if (film.getId() != null && films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Обновлен фильм: {}", film.toString());
        } else {
            log.debug("Ошибка обновления фильма: Фильм {} не найден", film.toString());
            throw new NoSuchFilmException("Фильм не найден");
        }

        return film;
    }
}
