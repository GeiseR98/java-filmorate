package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    User createUser(User user);

    User changeUser(User user);

    boolean isUserPresent(Integer id);

    void removeUsers(Integer id);

    User getUserById(Integer id);

    void addAsFriends(Integer oneId, Integer twoId);

    void removeFromFriends(Integer oneId, Integer twoId);

    List<User> getFriends(Integer id);

    List<User> getListOfMutualFriends(Integer oneId, Integer twoId);
}
