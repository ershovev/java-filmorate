package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class FilmControllerTest {

    UserStorage userStorage;
    FilmStorage filmStorage;
    FilmService filmService;
    FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        film = Film.builder()
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(2023, 10, 15))
                .duration(150)
                .build();
    }

    @Test
    void idShouldBe1() {
        filmController.create(film);
        assertEquals(1, film.getId());
    }

    @Test
    void shouldThrowValidationException() {
                film.setReleaseDate(LocalDate.of(1885, 12, 27));

        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                filmController.create(film);
            }
        });
        assertEquals("Дата выхода фильма раньше дня рождения кино - самой ранней возможной даты", exception.getMessage());
    }

    @Test
    void shouldThrowNoSuchFilmException() {
        film.setId(1);

        final NoSuchFilmException exception = assertThrows(NoSuchFilmException.class, new Executable() {
            @Override
            public void execute() {
                filmController.update(film);
            }
        });
        assertEquals("Фильм не найден", exception.getMessage());
    }
}
