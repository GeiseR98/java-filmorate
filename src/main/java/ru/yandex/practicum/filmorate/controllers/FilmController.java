package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        List<Film> list = new ArrayList<>();
        for (Integer key : films.keySet()) {
            list.add(films.get(key));
        }
        return list;
    }

    @PostMapping(value = "/film")
    public Film addFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
    @PutMapping(value = "/film")
    public Film changeFilm(@RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
