package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        for (Integer key : users.keySet()) {
            list.add(users.get(key));
        }
        return list;
    }

    @PostMapping(value = "/users")
    public User addFilm(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
    @PutMapping(value = "/users")
    public User changeFilm(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
