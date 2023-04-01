package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;
    private final Map<Integer, Set<Integer>> likes = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов: {}", films.values().size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++id);
        films.put(film.getId(), film);
        likes.put(film.getId(), new HashSet<>()); // вынести в отдельный метод
        log.debug("Фильм {} добавлен по номером {}.", film.getName(), id);
        return film;
    }

    @Override
    public Film changeFilm(Film film) {
        films.put(film.getId(), film);
        log.debug("Данные фильма под номером {} обновлены.", film.getId());
        return film;
    }

    @Override
    public void removeFilmById(Integer id) {

    }

    @Override
    public void addLike() {

    }

    @Override
    public void removeLike() {

    }

    @Override
    public List<Film> getPopularFilms() {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        return null;
    }
}
