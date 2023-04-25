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
        if (film.getId() != null && films.containsKey(film.getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFilmPresentById(Integer id) {
        if (films.containsKey(id)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Film getFilm(Integer id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
