package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserStorage userStorage;
    UserService userService;
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService((InMemoryUserStorage) userStorage);
        userController = new UserController(userService);
    }

    @Test
    void idShouldBe1() {
        User user = User.builder()
                .email("test@test.ru")
                .login("login")
                .birthday(LocalDate.of(2000, 12, 1))
                .build();
        userController.create(user);
        assertEquals(1, user.getId());
    }

    @Test
    void nameShouldBeAsLogin() {
        User user = User.builder()
                .email("test@test.ru")
                .login("login")
                .birthday(LocalDate.of(2000, 12, 1))
                .build();
        userController.create(user);
        assertEquals("login", user.getName());
    }

    @Test
    void shouldThrowNoSuchUserException() {

        User user = User.builder()
                .email("test@test.ru")
                .login("login")
                .birthday(LocalDate.of(2000, 12, 1))
                .build();
        userController.create(user);

        User updatedUser = User.builder()
                .id(2)
                .email("updated@test.ru")
                .login("loginUpdated")
                .birthday(LocalDate.of(2000, 12, 1))
                .build();

        final NoSuchUserException exception = assertThrows(NoSuchUserException.class, new Executable() {
            @Override
            public void execute() {
                userController.update(updatedUser);
            }
        });

        assertEquals("Пользователь не найден", exception.getMessage());
    }
}