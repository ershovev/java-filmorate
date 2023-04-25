package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

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
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public boolean isUserPresent(User user) {
        return userStorage.isUserPresent(user);
    }

    public boolean isUserPresentById(Integer id) {
        return userStorage.isUserPresentById(id);
        }

    public void addFriend(Integer userId, Integer friendId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        if (!isUserPresentById(friendId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        userStorage.getUser(userId).addFriend(friendId);   // добавляем айди друга юзеру
        userStorage.getUser(friendId).addFriend(userId);   // добавляем айди юзера другу
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (!isUserPresentById(userId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        if (!isUserPresentById(friendId)) {
            throw new NoSuchUserException("Пользователь не найден");
        }
        userStorage.getUser(userId).deleteFriend(friendId);  // удаляем айди друга из юзера
        userStorage.getUser(friendId).deleteFriend(userId);  // удаляаем айди юзера из друга
    }

    public List<User> getAllFriends(Integer userId) {
        Set<Integer> friends = userStorage.getUser(userId).getFriends(); // получение списка id всех друзей
        List<User> usersFriends = new ArrayList<>();

        for (User user : userStorage.findAll()) {
            if (friends.contains(user.getId())) {
                usersFriends.add(user);
            }
        }
        return usersFriends;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        Set<Integer> userFriends = new HashSet<>(userStorage.getUser(userId).getFriends());
        Set<Integer> otherUserFriends = new HashSet<>(userStorage.getUser(otherUserId).getFriends());
        userFriends.retainAll(otherUserFriends); // фильтруем по общим друзьям

        List<User> commonFriends = new ArrayList<>();

        for (User user : userStorage.findAll()) {
            if (userFriends.contains(user.getId())) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }
}