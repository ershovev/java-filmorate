package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchMpaException extends RuntimeException {
    public NoSuchMpaException(String errorMessage) {
        super(errorMessage);
    }
}
