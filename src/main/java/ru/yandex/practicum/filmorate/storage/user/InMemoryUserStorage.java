package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(id);
        users.put(id, user);
        id++;

        return user;
    }

    @Override
    public User update(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean isUserPresent(User user) {
        return user.getId() != null && users.containsKey(user.getId());
    }

    @Override
    public boolean isUserPresentById(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public User getUser(Integer id) {
        return users.get(id);
    }
}
