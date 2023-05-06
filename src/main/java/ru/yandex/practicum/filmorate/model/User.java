package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Integer id;
    @Email
    private final String email;
    @Pattern(regexp = "[a-zA-Z0-9]*")
    private final String login;
    private String name;
    @PastOrPresent
    private final LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public void addFriend(Integer id) {  // добавление айди друга
        friends.add(id);
    }

    public void deleteFriend(Integer id) {  // удаление айди друга
        friends.remove(id);
    }
}
