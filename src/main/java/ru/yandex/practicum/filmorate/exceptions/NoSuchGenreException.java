package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchGenreException extends RuntimeException {
    public NoSuchGenreException(String errorMessage) {
        super(errorMessage);
    }
}
