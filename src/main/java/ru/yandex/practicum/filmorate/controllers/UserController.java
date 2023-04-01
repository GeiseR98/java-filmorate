package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
    public void addAsFriends(@PathVariable("userId") Integer oneId, @PathVariable("friendId") Integer twoId) {
        log.info("Получен запрос на добавление в друзья пользователя {} пользователем {}", oneId, twoId);
        userService.addAsFriends(oneId, twoId);
    }
    @DeleteMapping("users/{userId}/friends/{friendId}")
    public void removeFromFriends(@PathVariable("userId") Integer oneId, @PathVariable("friendId") Integer twoId) {
        log.info("Получен запрос на удаление из друзей {} пользователем {}", oneId, twoId);
        userService.removeFromFriends(oneId, twoId);
    }
    @GetMapping("/users/{userId}/friends")
    public List<User> getFriends(@PathVariable("userId") Integer id) {
        log.info("получен запрос списка друзей пользователя с идентификатором {}", id);
        return userService.getFriends(id);
    }
    @GetMapping("/users/{userId}/friends/common/{otherId}")
    public List<User> getListOfMutualFriends(
            @PathVariable("userId") Integer oneId,
            @PathVariable("otherId") Integer twoId) {
        log.info("Получен запрос списка общих друзей");
        return userService.getListOfMutualFriends(oneId, twoId);
    }
}
