package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void idShouldBe1() throws ValidationException {
        Film film = Film.builder()
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(2023, 10, 15))
                .duration(150)
                .build();
        filmController.create(film);
        assertEquals(1, film.getId());
    }

    @Test
    void shouldThrowValidationException() {
        Film film = Film.builder()
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(150)
                .build();

        final ValidationException exception = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.create(film);
            }
        });
        assertEquals("Дата выхода фильма раньше дня рождения кино", exception.getMessage());
    }

    @Test
    void shouldThrowNoSuchFilmException() {
        Film film = Film.builder()
                .id(1)
                .name("TestFilm")
                .description("Test Description")
                .releaseDate(LocalDate.of(1995, 12, 27))
                .duration(150)
                .build();

        final NoSuchFilmException exception = assertThrows(NoSuchFilmException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filmController.update(film);
            }
        });
        assertEquals("Фильм не найден", exception.getMessage());
    }
}
