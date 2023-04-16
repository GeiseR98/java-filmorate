package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

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
        userStorage.isUserPresent(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.changeUser(user);
    }

    public User getUserById(Integer id) {
        userStorage.isUserPresent(id);
        return userStorage.getUserById(id);
    }

    public void removeUsers(Integer id) {
        userStorage.isUserPresent(id);
        userStorage.removeUsers(id);
    }

    public void addAsFriends(Integer oneId, Integer twoId) {
        userStorage.isUserPresent(oneId);
        userStorage.isUserPresent(twoId);
        userStorage.addAsFriends(oneId, twoId);
    }

    public void removeFromFriends(Integer oneId, Integer twoId) {
        userStorage.isUserPresent(oneId);
        userStorage.isUserPresent(twoId);
        userStorage.removeFromFriends(oneId, twoId);
    }

    public List<User> getFriends(Integer id) {
        userStorage.isUserPresent(id);
        return userStorage.getFriends(id);
    }

    public List<User> getListOfMutualFriends(Integer oneId, Integer twoId) {
        userStorage.isUserPresent(oneId);
        userStorage.isUserPresent(twoId);
        return userStorage.getFriends(oneId).stream()
                .filter(userStorage.getFriends(twoId)::contains)
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
