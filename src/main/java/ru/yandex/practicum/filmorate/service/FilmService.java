package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

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
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public boolean isFilmPresent(Film film) {
        return filmStorage.isFilmPresent(film);
    }

    public boolean isFilmPresentById(Integer id) {
        return filmStorage.isFilmPresentById(id);
    }

    public void addLike(Integer userId, Integer filmId) {
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void removeLike(Integer userId, Integer filmId) {
        if (!filmStorage.getFilm(filmId).getLikedUsersIds().contains(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        filmStorage.getFilm(filmId).removeLike(userId);
    }

    public List<Film> getMostLikedFilms(Integer size) {
        List<Film> mostLikedFilms = filmStorage.getAllFilms();
        return mostLikedFilms.stream()
                .sorted((f1, f2) -> Integer.valueOf(f2.getLikedUsersIds().size()) // сортируем по количеству лайков
                        .compareTo(f1.getLikedUsersIds().size()))
                .limit(size)
                .collect(Collectors.toList());
    }
}
