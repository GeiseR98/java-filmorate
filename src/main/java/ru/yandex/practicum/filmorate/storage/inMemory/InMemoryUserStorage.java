package ru.yandex.practicum.filmorate.storage.inMemory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();
    private int id = 0;

    @Override
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.values().size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>()); // вынести в отдельный метод
        log.debug("Пользователь добавлен под номером: " + user.getId());
        return user;
    }

    @Override
    public User changeUser(User user) {
        users.put(user.getId(), user);
        log.debug("Данные пользователя под номером: " + user.getId() + " обновлены");
        return user;
    }

    @Override
    public boolean isUserPresent(Integer id) {
        if (!users.containsKey(id)) {
            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
        }
        return true;
    }

    @Override
    public void removeUsers(Integer id) {
        log.debug("Пользователь с идентификатором {} удалён.", id);
        if (!friends.get(id).isEmpty()) {
            for (Integer friendId : friends.get(id)) {
                friends.get(friendId).remove(id);
            }
        }
        users.remove(id);
    }

    @Override
    public User getUserById(Integer id) {
        return users.get(id);
    }

    @Override
    public void addAsFriends(Integer oneId, Integer twoId) {
        log.debug("Пользователи с идентификаторами {} и {} стали друзьями", oneId, twoId);
        friends.get(oneId).add(twoId);
        friends.get(twoId).add(oneId);
    }

    @Override
    public void removeFromFriends(Integer oneId, Integer twoId) {
        log.debug("Пользователи с идентификаторами {} и {} перестали дружить", oneId, twoId);
        friends.get(oneId).remove(twoId);
        friends.get(twoId).remove(oneId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        log.debug("Текущее колличество друзей у пользователя с идентификатором {}: {}", id, friends.get(id).size());
        isUserPresent(id);
        return friends.get(id).stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getListOfMutualFriends(Integer oneId, Integer twoId) {
        isUserPresent(oneId);
        isUserPresent(twoId);
        return friends.get(oneId).stream()
                .filter(friends.get(twoId)::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }
}
