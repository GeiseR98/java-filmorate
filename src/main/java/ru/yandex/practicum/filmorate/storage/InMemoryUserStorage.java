package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 0;
    private final Map<Integer, Set<Integer>> friends = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.values().size());
        return new ArrayList<>(users.values());
    }
    @Override
    public User createUser(@Valid @RequestBody User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        friends.put(user.getId(), new HashSet<>()); // вынести в отдельный метод
        log.debug("Пользователь добавлен под номером: " + user.getId());
        return user;
    }
    @Override
    public User changeUser(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        log.debug("Данные пользователя под номером: " + user.getId() + " обновлены");
        return user;
    }
    @Override
    public boolean isUserPresent(Integer id) {
        return users.containsKey(id);
    }

    @Override
    public void removeUsers(Integer id) {
        log.debug("Пользователь с идентификатором {} удалён.", id);
        users.remove(id);
        // добавить логику по удалению из друзей в сервис
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
    public List<Integer> getFriends(Integer id) {
        log.debug("Текущее колличество друзей у пользователя с идентификатором {}: {}", id, friends.get(id).size());
        return new ArrayList<>(friends.get(id));
    }

}