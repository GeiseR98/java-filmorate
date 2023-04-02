package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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
            log.debug("Фильм с номером {} не найден", film.getId());
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", film.getId()));
        }
    }

    public Film getFilmById (Integer filmId) {
        if (filmStorage.isFilmPresent(filmId)) {
            return filmStorage.getFilmById(filmId);
        } else {
            log.debug("Фильм с номером {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", filmId));
        }
    }
    public void removeFilms(Integer filmId) {
        if (filmStorage.isFilmPresent(filmId)) {
            filmStorage.removeFilmById(filmId);
        } else {
            log.debug("Фильм с номером {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", filmId));
        }
    }
    public void addLike(Integer userId, Integer filmId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
        if (!filmStorage.isFilmPresent(filmId)) {
            log.debug("Фильм с номером {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", filmId));
        }
        filmStorage.addLike(userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
//        if (!userStorage.isUserPresent(oneId)) {
//            log.debug(String.format("Пользователь с идентификатором %s не найден", oneId));
//            throw new UserNotFoundException(String.format("Пользователь с идентификатором %s не найден", oneId));
//        }
        if (!filmStorage.isFilmPresent(filmId)) {
            log.debug("Фильм с номером {} не найден", filmId);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", filmId));
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(Objects.requireNonNullElse(count, 10));
    }
}
