package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUser(Integer id) {
        if (!isUserPresentById(id)) {
            throw new NoSuchUserException("Пользователь не найден");
        }

        return userStorage.getUser(id);
    }

    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        User createdUser = userStorage.create(user);
        log.debug("Добавлен пользователь: {}", createdUser.toString());
        return createdUser;
    }

    public User update(User user) {
        User userToReturn;
        if (isUserPresent(user)) {
            userToReturn = userStorage.update(user);
            log.debug("Обновлен пользователь: {}", user.toString());
        } else {
            log.debug("Ошибка обновления пользователя: Пользователь {} не найден", user.toString());
            throw new NoSuchUserException("Пользователь не найден");
        }

        return userToReturn;
    }

    public boolean isUserPresent(User user) {
        return userStorage.isUserPresent(user);
    }

    public boolean isUserPresentById(Integer id) {
        return userStorage.isUserPresentById(id);
    }

    public void addFriend(Integer userId, Integer otherUserId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        if (!isUserPresentById(otherUserId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }

        userStorage.addFriend(userId, otherUserId);
    }

    public void deleteFriend(Integer userId, Integer otherUserId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        if (!isUserPresentById(otherUserId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }

        userStorage.deleteFriendshipFromEachOther(userId, otherUserId);
    }

    public List<User> getAllFriends(Integer userId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }

        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        if (!isUserPresentById(otherUserId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }

        return userStorage.getCommonFriends(userId, otherUserId);
    }
}