package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("Не прошла валидация по дате релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return filmService.changeFilm(film);
    }

}
