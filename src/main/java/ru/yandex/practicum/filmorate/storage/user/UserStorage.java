package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAll(); // получение всех пользователей
    User create(User user); // создание пользователя

    User update(User user); // обновление пользователя

    boolean isUserPresent(User user); // проверка существует ли пользователь

    boolean isUserPresentById(Integer id);

    User getUser(Integer id); // получить пользователя
}
