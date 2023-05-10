package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {
    private static final LocalDate EARLIEST_POSSIBLE_FILM_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    private final FilmStorage filmStorage;

    @Autowired
    private final UserStorage userStorage;

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getFilm(Integer id) {
        if (!isFilmPresentById(id)) {
            throw new NoSuchFilmException("Фильм не найден");
        }
        return filmStorage.getFilm(id);
    }

    public Film create(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_POSSIBLE_FILM_RELEASE_DATE)) {
            log.debug("Ошибка добавления фильма: Дата выхода фильма раньше дня рождения кино - самой ранней возможной даты");
            throw new ValidationException("Дата выхода фильма раньше дня рождения кино - самой ранней возможной даты");
        }
        Film addedFilm = filmStorage.create(film);
        log.debug("Добавлен фильм: {}", addedFilm.toString());
        return addedFilm;
    }

    public Film update(Film film) {
        Film updatedFilm;
        if (isFilmPresent(film)) {
            updatedFilm = filmStorage.update(film);
            log.debug("Обновлен фильм: {}", film.toString());
        } else {
            log.debug("Ошибка обновления фильма: Фильм {} не найден", film.toString());
            throw new NoSuchFilmException("Фильм не найден");
        }
        return updatedFilm;
    }

    public boolean isFilmPresent(Film film) {
        return filmStorage.isFilmPresent(film);
    }

    public boolean isFilmPresentById(Integer id) {
        return filmStorage.isFilmPresentById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        if (!isFilmPresentById(filmId)) {
            throw new NoSuchFilmException("Фильм не найден");
        }
        if (!userStorage.isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        if (!isFilmPresentById(filmId)) {
            throw new NoSuchFilmException("Фильм не найден");
        }
        if (!userStorage.isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostLikedFilms(Integer size) {
        return filmStorage.getMostLikedFilms(size);
    }
}
