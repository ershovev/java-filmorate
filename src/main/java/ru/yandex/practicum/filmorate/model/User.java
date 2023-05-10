package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> toMap() {  // метод создания хэшмапы с данными пользователя
        Map<String, Object> values = new HashMap<>();  // для использования в simpleJdbcInsert
        values.put("email", email);
        values.put("login", login);
        values.put("name", name);
        values.put("birthday", birthday);
        return values;
    }
}
