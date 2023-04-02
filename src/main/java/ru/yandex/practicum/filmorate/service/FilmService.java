package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            log.debug(String.format("Фильм с идентификатором {} не найден", film.getId()));
            throw new FilmNotFoundException(String.format("Фильм с идентификатором {} не найден", film.getId()));
        }
    }

    public Film getFilmById (Integer id) {
        if (filmStorage.isFilmPresent(id)) {
            return filmStorage.getFilmById(id);
        } else {
            log.debug("Фильм с номером {} не найден", id);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", id));
        }
    }
    public void removeFilms(Integer id) {
        if (filmStorage.isFilmPresent(id)) {
            filmStorage.removeFilmById(id);
        } else {
            log.debug(String.format("Пользователь с идентификатором %s не найден", id));
            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", id));
        }
    }
    public void addLike(Integer userId, Integer filmId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
        if (!filmStorage.isFilmPresent(filmId)) {
            log.debug("Пользователю {} понравился фильм {}.", userId, filmId);
            throw new FilmNotFoundException(String.format("Пользователь с идентификатором %s не найден", filmId));
        }
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
        if (!filmStorage.isFilmPresent(filmId)) {
            log.debug("Пользователю {} понравился фильм {}.", userId, filmId);
            throw new FilmNotFoundException(String.format("Пользователь с идентификатором %s не найден", filmId));
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(Objects.requireNonNullElse(count, 10));
    }
}
