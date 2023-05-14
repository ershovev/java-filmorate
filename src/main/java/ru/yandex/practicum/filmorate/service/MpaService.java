package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchMpaException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MpaService {

    private final MpaStorage mpaStorage;

    public List<Mpa> findAll() {
        return mpaStorage.findAll();
    }

    public Mpa getMpa(Integer id) {
        if (!isMpaPresentById(id)) {
            throw new NoSuchMpaException("MPA не найден");
        }

        return mpaStorage.getMpa(id);
    }

    public boolean isMpaPresentById(Integer id) {
        return mpaStorage.isMpaPresentById(id);
    }
}
