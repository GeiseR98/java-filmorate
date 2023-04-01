package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }
    public Film addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Не прошла валидация по дате релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return filmStorage.addFilm(film);
    }
    public Film changeFilm(Film film) {
        if (filmStorage.isFilmPresent(film.getId())) {
            return filmStorage.changeFilm(film);
        } else {
            log.debug(String.format("Фильм с идентификатором {} не найдено", film.getId()));
            throw new UserNotFoundException(String.format("Фильм с идентификатором {} не найдено", film.getId()));
        }
    }

//    public User changeUser(User user) {
//        if (userStorage.isUserPresent(user.getId())) {
//            if (user.getName() == null || user.getName().isBlank()) {
//                user.setName(user.getLogin());
//            }
//            return userStorage.changeUser(user);
//        } else {
//            log.debug("данный пользователь не обнаружен");
//            throw new ValidationException("данный пользователь не обнаружен");
//        }
//    }
//    public User getUserById (Integer id) {
//        if (userStorage.isUserPresent(id)) {
//            return userStorage.getUserById(id);
//        } else {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
//        }
//    }
//    public void removeUsers(Integer id) {
//        if (userStorage.isUserPresent(id)) {
//            userStorage.removeUsers(id);
//        } else {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
//        }
//    }
//    public void addAsFriends(Integer oneId, Integer twoId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
//        if (!userStorage.isUserPresent(twoId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", twoId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", twoId));
//        }
//        userStorage.addAsFriends(oneId, twoId);
//    }
//    public void removeFromFriends(Integer oneId, Integer twoId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
//        if (!userStorage.isUserPresent(twoId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", twoId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", twoId));
//        }
//        userStorage.removeFromFriends(oneId, twoId);
//    }
//    public List<User> getFriends(Integer id){
//        if (!userStorage.isUserPresent(id)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
//        }
//        return userStorage.getFriends(id).stream()
//                .map(userStorage::getUserById)
//                .collect(Collectors.toList());
//    }
//    public List<User> getListOfMutualFriends(Integer oneId,Integer twoId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
//        if (!userStorage.isUserPresent(twoId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", twoId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", twoId));
//        }
//        return userStorage.getFriends(oneId).stream()
//                .filter(userStorage.getFriends(twoId)::contains)
//                .map(userStorage::getUserById)
//                .collect(Collectors.toList());
//    }
}
