package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    List<User> getAllUsers();
    User createUser(User user);
    User changeUser(User user);
    boolean isUserPresent(Integer id);
    void removeUsers(Integer id);
    User getUserById(Integer id);
    void addAsFriends(Integer oneId, Integer twoId);
    void removeFromFriends(Integer oneId, Integer twoId);
    Set<Integer> getFriends(Integer id);

    /*
    для переноса ("рефакторинга") уже есть
    findAll
    addUser
    changeUser

    новое
    removeUser
    addFriend
    RemoveFriends
    getUserById
    getFriends
     */
}
