package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.debug("Получен запрос всех фильмов");
        return filmService.getAllFilms();
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос на добавление фильма {}.", film.getName());
        return filmService.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film changeFilm(@Valid @RequestBody Film film) {
        log.debug("Получен запрос на добавление фильма {}.", film.getName());
        return filmService.changeFilm(film);
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmById(@PathVariable int filmId) {
        log.debug("Получен запрос фильма по номеру");
        return filmService.getFilmById(filmId);
    }

    @DeleteMapping("/films/{filmId}")
    public void removeFilmById(@PathVariable int filmId) {
        log.debug("Получен запрос на удаление фильма номер {}", filmId);
        filmService.removeFilms(filmId);
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Получен запрос на добавления лайка фильму {} пользователем {}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId, @PathVariable int userId) {
        log.debug("Получен запрос на удаление фильма");
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "В запросе задано отрицательное или равное нолю колличество фильмов")

            Integer count) {
        log.debug("Получен запрос топ-{} популярных фильмов", count);
        return filmService.getPopularFilms(count);
    }
}
