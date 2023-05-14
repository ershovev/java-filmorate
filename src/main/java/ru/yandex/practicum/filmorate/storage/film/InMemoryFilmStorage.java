package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @Override
    public Film create(Film film) {  // добавить фильм
        film.setId(id);
        films.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film update(Film film) {  // обновить фильм
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> findAll() {  // получить список всех фильмов
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean isFilmPresent(Film film) {
        return film.getId() != null && films.containsKey(film.getId());
    }

    @Override
    public boolean isFilmPresentById(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public Film getFilm(Integer id) {
        return films.get(id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
    }

    @Override
    public List<Film> getMostLikedFilms(Integer size) {
        return null;
    }
}

