package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        log.debug("запрос всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        log.info("Получен запрос POST для создания нового пользователя.");
        return userService.createUser(user);
    }

    @PutMapping(value = "/users")
    public User changeUser(@Valid @RequestBody User user) {
        log.info("Получен запрос PUT для обновления существующего пользователя.");
        return userService.changeUser(user);
    }
    @GetMapping("/users/{id}")
        public User getUserById(@PathVariable Integer id) {
        log.info("получен запрос пользователя по идентификатору");
        return userService.getUserById(id);
    }
    @DeleteMapping("/users/{id}")
    public void removeUser(@PathVariable Integer id) {
        log.info("получен запрос на удаление пользователя.");
        userService.removeUsers(id);
    }
    @PutMapping("/users/{userId}/friends/{friendId}")
    public void addAsFriends(@PathVariable("userID") Integer oneId, @PathVariable("friendId") Integer twoId) {
        log.info("Получен запрос на добавление в друзья пользователя {} пользователем {}", oneId, twoId);
        userService.addAsFriends(oneId, twoId);
    }
    @DeleteMapping("users/{userId}/friends/{friendId}")
    public void removeFromFriends(Integer oneId, Integer twoId) {
        log.info("Получен запрос на удаление из друзей {} пользователем {}", oneId, twoId);
        userService.removeFromFriends(oneId, twoId);
    }
//    List<Integer> getFriends(Integer id);
}
