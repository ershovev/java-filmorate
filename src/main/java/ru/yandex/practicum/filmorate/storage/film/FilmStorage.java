package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create (Film film); // добавить фильм
    Film update (Film film); // обновить фильм
    List<Film> findAll(); // получить список всех фильмов
    boolean isFilmPresent(Film film); // присутствует ли фильм в базе

    boolean isFilmPresentById(Integer id);

    Film getFilm(Integer id); // получить фильм
    List<Film> getAllFilms(); // получить все фильмы
}
