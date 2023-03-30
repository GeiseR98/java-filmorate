package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.values().size());
        return new ArrayList<>(users.values());
    }
    @Override
    public User createUser(@Valid @RequestBody User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен под номером: " + user.getId());
        return user;
    }
    @Override
    public User changeUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        log.debug("Данные пользователя под номером: " + user.getId() + " обновлены");
        return user;
    }
    @Override
    public boolean isUserPresent(Integer id) {
        return users.containsKey(id);
    }
}
