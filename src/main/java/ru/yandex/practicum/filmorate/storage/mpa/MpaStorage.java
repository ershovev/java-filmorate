package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {
    List<Mpa> findAll(); // получить список всех MPA

    Mpa getMpa(Integer id); // получить MPA

    boolean isMpaPresentById(Integer id);
}
