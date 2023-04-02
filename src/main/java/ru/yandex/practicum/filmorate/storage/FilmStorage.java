package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    Film addFilm(Film film);
    Film changeFilm(Film film);
    void removeFilmById(Integer id);
    void addLike(Integer filmId, Integer userId);
    void removeLike(Integer filmId, Integer userId);
    List<Film> getPopularFilms(Integer count);
    Film getFilmById(Integer id);
    boolean isFilmPresent(Integer id);
    /*
    для переноса ("рефакторинга") уже есть
    findAll
    addFilm
    changeFilm
/*
    новое
    removeFilm
    addLike
    removeLike
    getPopularByCount
    getFilmById
     */
}
