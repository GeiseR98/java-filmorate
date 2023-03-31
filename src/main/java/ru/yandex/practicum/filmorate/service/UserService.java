package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.createUser(user);
    }
    public User changeUser(User user) {
        if (userStorage.isUserPresent(user.getId())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return userStorage.changeUser(user);
        } else {
            log.debug("данный пользователь не обнаружен");
            throw new ValidationException("данный пользователь не обнаружен");
        }
    }
    public User getUserById (Integer id) {
        if (userStorage.isUserPresent(id)) {
            return userStorage.getUserById(id);
        } else {
            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
        }
    }
    public void removeUsers(Integer id) {
        if (userStorage.isUserPresent(id)) {
            userStorage.removeUsers(id);
        } else {
            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
        }
    }
    public void addAsFriends(Integer oneId, Integer twoId) {
        if (!userStorage.isUserPresent(oneId)) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
        }
        if (!userStorage.isUserPresent(twoId)) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", twoId));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", twoId));
        }
        userStorage.addAsFriends(oneId, twoId);
    }
    public void removeFromFriends(Integer oneId, Integer twoId) {
        if (!userStorage.isUserPresent(oneId)) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
        }
        if (!userStorage.isUserPresent(twoId)) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", twoId));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", twoId));
        }
        userStorage.removeFromFriends(oneId, twoId);
    }
//    List<Integer> getFriends(Integer id);

}
