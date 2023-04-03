package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film changeFilm(Film film) {
        filmStorage.isFilmPresent(film.getId());
        return filmStorage.changeFilm(film);

    }

    public Film getFilmById(Integer filmId) {
        filmStorage.isFilmPresent(filmId);
        return filmStorage.getFilmById(filmId);
    }

    public void removeFilms(Integer filmId) {
        filmStorage.isFilmPresent(filmId);
        filmStorage.removeFilmById(filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        userStorage.isUserPresent(userId);
        filmStorage.isFilmPresent(filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        userStorage.isUserPresent(userId);
        filmStorage.isFilmPresent(filmId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(Objects.requireNonNullElse(count, 10));
    }
}
