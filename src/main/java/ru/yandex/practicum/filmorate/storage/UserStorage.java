package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();
    User createUser(User user);
    User changeUser(User user);
    boolean isUserPresent(Integer id);
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
