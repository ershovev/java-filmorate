package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAll() {    // получить всех пользователей
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping("/users")
    public User create(@Valid @RequestBody User user) {   // создать пользователя
        User createdUser = userService.create(user);
        log.debug("Добавлен пользователь: {}", createdUser.toString());
        return createdUser;
    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {  // обновить пользователя
        if (userService.isUserPresent(user)) {
            userService.update(user);
            log.debug("Обновлен пользователь: {}", user.toString());
        } else {
            log.debug("Ошибка обновления пользователя: Пользователь {} не найден", user.toString());
            throw new NoSuchUserException("Пользователь не найден");
        }

        return user;
    }

    @PutMapping("/users/{id}/friends/{friendId}")     // добавить пользователей в друзья друг другу
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")  // удалить пользователей из друзей друг друга
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.deleteFriend(id, friendId);
    }
}
