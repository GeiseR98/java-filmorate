package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;

import java.util.List;
import java.util.Set;

@Repository
@Primary
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
        return null;
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User changeUser(User user) {
        return null;
    }

    @Override
    public boolean isUserPresent(Integer id) {
        return false;
    }

    @Override
    public void removeUsers(Integer id) {

    }

    @Override
    public User getUserById(Integer id) {
        return null;
    }

    @Override
    public void addAsFriends(Integer oneId, Integer twoId) {

    }

    @Override
    public void removeFromFriends(Integer oneId, Integer twoId) {

    }

    @Override
    public Set<Integer> getFriends(Integer id) {
        return null;
    }
}
