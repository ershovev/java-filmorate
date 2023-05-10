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

    void addFriend(Integer userId, Integer friendId);

    void deleteFriendshipFromEachOther(Integer userId, Integer otherUserId);

    List<User> getAllFriends(Integer id);

    List<User> getCommonFriends(Integer userId, Integer otherUserId);
}
