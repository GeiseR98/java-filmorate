package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, userMapper);
    }

    @Override
    public User getUserById(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, userMapper, id);
    }

    @Override
    public User createUser(User user) {
        String sqlQuery = "INSERT INTO users (name, email, login, birthday)" +
                "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId((Integer) keyHolder.getKey());
        }
        return user;
    }

    @Override
    public User changeUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "name = ?, email = ?, login = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }

    @Override
    public void removeUsers(Integer id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addAsFriends(Integer oneId, Integer twoId) { // добавить проверку и ветвление
        isUserPresent(oneId);
        isUserPresent(twoId);
        String sqlQuery = "INSERT INTO friends (friend_one_id, friend_two_id, status) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, oneId, twoId, false);
    }

    @Override
    public List<User> getFriends(Integer id) { // запрос вернуть друзей переделать
        String sqlQuery = "SELECT * FROM users " +
                "WHERE user_id IN (" +
                "SELECT friend_two_id " +
                "FROM friends WHERE friend_one_id = ?)";
        return jdbcTemplate.query(sqlQuery, userMapper, id);
    }

    @Override
    public List<User> getListOfMutualFriends(Integer oneId, Integer twoId) {
        String sqlQuery = "SELECT *" +
                "FROM users " +
                "WHERE user_id IN (SELECT friend_two_id " +
                "FROM friends AS f1 " +
                "WHERE friend_one_id = ? " +
                "AND friend_two_id IN ( " +
                "SELECT friend_two_id " +
                "FROM friends AS f2 " +
                "WHERE friend_one_id = ?))";
        return jdbcTemplate.query(sqlQuery, userMapper, oneId, twoId);
    }

    @Override
    public boolean isUserPresent(Integer id) {
        String query = "SELECT COUNT (*) FROM users WHERE user_id = ?";
        if (jdbcTemplate.queryForObject(query, Integer.class, id) == 0) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
        }
        return true;
    }

    @Override
    public void removeFromFriends(Integer oneId, Integer twoId) {
        String sqlQuery = "DELETE FROM friends WHERE friend_one_id = ? AND friend_two_id = ?";
        jdbcTemplate.update(sqlQuery, oneId, twoId);
    }

}
