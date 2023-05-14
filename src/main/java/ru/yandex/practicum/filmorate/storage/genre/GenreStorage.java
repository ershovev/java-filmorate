package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAll(); // получить список всех жанров

    Genre getGenre(Integer id); // получить жанр

    boolean isGenrePresentById(Integer id);
}
