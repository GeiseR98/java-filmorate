package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        for (Integer key : users.keySet()) {
            list.add(users.get(key));
        }
        log.debug("запрос всех пользователей");
        return list;
    }
    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.debug("Пользователь добавлен под номером: " + user.getId());
        return user;
    }
    @PutMapping(value = "/users")
    public User changeUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.debug("Данные пользователя под номером: " + user.getId() + " обновлены");
            return user;
        } else {
            return addUser(user);
        }
    }
}
