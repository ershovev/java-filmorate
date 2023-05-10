package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser); // вызываем mapRowToUser и преобразуем строки в объекты User
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")    // insert в таблицу БД users
                .usingGeneratedKeyColumns("id");  // получаем сгенерированный БД id пользователя
        Integer userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue(); // сохраняем юзера в БД и получаем id пользователя
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ?" +
                " WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return getUser(user.getId());
    }

    @Override
    public boolean isUserPresent(User user) {
        return isUserPresentById(user.getId());
    }

    @Override
    public boolean isUserPresentById(Integer id) {
        String sqlQuery = "SELECT id " +
                "FROM users WHERE id = ?;";
        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{id}, (ResultSet rs) -> {
            return rs.next();
        }); // проверяем есть ли строка в ответе
        return exists;
    }

    @Override
    public User getUser(Integer id) {
        String sqlQuery = "SELECT id, email, login, name, birthday " +
                "FROM users WHERE id = ?;";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id); // отправляем запрос и вызываем mapRowToUser         // чтобы преобразовать ответ от БД в объект User
    }

    @Override
    public void addFriend(Integer requester_id, Integer requestee_id) {
        String sqlQuery = "SELECT requester_id " +   //
                "FROM friendship WHERE requester_id = ? AND requestee_id = ? AND FRIENDSHIP_STATUS = 'Not confirmed';";

        boolean exists = jdbcTemplate.query(sqlQuery, new Object[]{requestee_id, requester_id}, (ResultSet rs) -> {
            return rs.next();
        }); // проверяем есть ли запись о неподтвержденной дружбе
        if (exists) {   // если запись существует то подтверждаем ранее поданную заявку от второго пользователя
            String sqlQueryForUpdate = "UPDATE friendship SET friendship_status = ? " +
                    "WHERE requester_id = ? AND requestee_id = ?;";
            jdbcTemplate.update(sqlQueryForUpdate, "Confirmed", requestee_id, requester_id);
        } else {  // если записи не существует то создаем со статусом Not confirmed
            String sqlQueryForCreate = "INSERT INTO friendship(requester_id, requestee_id, friendship_status) " +
                    "values (?, ?, ?)";
            jdbcTemplate.update(sqlQueryForCreate, requester_id, requestee_id, "Not confirmed");
        }
    }

    @Override
    public void deleteFriendshipFromEachOther(Integer userId, Integer otherUserId) {
        String sqlQuery = "DELETE FROM friendship WHERE requester_id = ? AND requestee_id = ?;" +
                " DELETE FROM friendship WHERE requester_id = ? AND requestee_id = ?;";
        jdbcTemplate.update(sqlQuery, userId, otherUserId, otherUserId, userId);
    }

    @Override
    public List<User> getAllFriends(Integer id) {   // получить всех друзей пользователя
        String sqlQuery = "SELECT *" +
                " FROM users" +
                " WHERE id IN (SELECT requestee_id" +
                " FROM friendship" +
                " WHERE requester_id = ?" +
                " UNION" +
                " SELECT requester_id" +
                " FROM friendship" +
                " WHERE requestee_id = ? AND friendship_status = 'Confirmed');";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, id);   //вызываем mapRowToUser чтобы преобразовать
        //каждую строку в объект User
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        String sqlQuery = "SELECT *" +
                " FROM users" +
                " WHERE id IN (SELECT requestee_id" +
                "             FROM friendship" +
                "             WHERE requester_id = ?" +
                "             UNION" +
                "             SELECT requester_id" +
                "             FROM friendship" +
                "             WHERE requestee_id = ? AND friendship_status = 'Confirmed')" +
                " AND id IN (SELECT requestee_id" +
                "             FROM friendship" +
                "             WHERE requester_id =?" +
                "             UNION" +
                "             SELECT requester_id" +
                "             FROM friendship" +
                "             WHERE requestee_id = ? AND friendship_status = 'Confirmed');";

        List<User> users;

        users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, userId, userId, otherUserId, otherUserId);   //вызываем mapRowToUser чтобы преобразовать
        //каждую строку в объект User

        return users;
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException { // метод создания объекта User из ответа БД
        User user = User.builder()
                .id(resultSet.getInt("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(LocalDate.parse(resultSet.getString("birthday")))
                .build();

        return user;
    }
}
