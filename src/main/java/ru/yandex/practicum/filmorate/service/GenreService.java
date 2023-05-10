package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchGenreException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;


import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GenreService {

    @Autowired
    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.findAll();
    }

    public Genre getGenre(Integer id) {
        if (!isGenrePresentById(id)) {
            throw new NoSuchGenreException("Жанр не найден");
        }
        return genreStorage.getGenre(id);
    }

    public boolean isGenrePresentById(Integer id) {
        return genreStorage.isGenrePresentById(id);
    }
}
