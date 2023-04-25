package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private static final LocalDate EARLIEST_POSSIBLE_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final String DEFAULT_AMOUNT_OF_MOST_POPULAR_FILMS = "10";

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = DEFAULT_AMOUNT_OF_MOST_POPULAR_FILMS) Integer count) {
            return filmService.getMostLikedFilms(count);
        }

    @PostMapping("/films")
    public Film create(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_POSSIBLE_FILM_RELEASE_DATE)) {
            log.debug("Ошибка добавления фильма: Дата выхода фильма раньше дня рождения кино - самой ранней возможной даты");
            throw new ValidationException("Дата выхода фильма раньше дня рождения кино - самой ранней возможной даты");
        }
        Film addedFilm = filmService.create(film);
        log.debug("Добавлен фильм: {}", addedFilm.toString());
        return addedFilm;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        if (filmService.isFilmPresent(film)) {
            filmService.update(film);
            log.debug("Обновлен фильм: {}", film.toString());
        } else {
            log.debug("Ошибка обновления фильма: Фильм {} не найден", film.toString());
            throw new NoSuchFilmException("Фильм не найден");
        }

        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(userId, id);
    }
}
