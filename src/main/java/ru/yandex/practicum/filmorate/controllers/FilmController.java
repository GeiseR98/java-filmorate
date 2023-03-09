package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping("/films")
    public List<Film> findAll() {
        List<Film> list = new ArrayList<>();
        for (Integer key : films.keySet()) {
            list.add(films.get(key));
        }
        log.debug("запрос всех фильмов");
        return list;
    }

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.debug("не прошла валидация по дате релиза");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        ++id;
        film.setId(id);
        films.put(film.getId(), film);
        log.debug("Фильм добавлен под номером: " + film.getId());
        return film;
    }
    @PutMapping(value = "/films")
    public Film changeFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.debug("Фильм под номером: " + film.getId() + " обновлён");
            return film;
        } else {
            throw new ValidationException("Фильм под данным номером не найден");
        }
    }
}
