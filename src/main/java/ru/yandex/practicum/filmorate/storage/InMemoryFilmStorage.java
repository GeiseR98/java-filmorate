package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

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
    public boolean isFilmPresent(Integer id) {
        return films.containsKey(id);
    }

    @Override
    public void removeFilmById(Integer id) {
        films.remove(id);
        likes.remove(id);
        log.debug("Фильм с номером {} удалён.", id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        log.debug("Пользователь {} поставил лайк фильму {}", userId, filmId);
        likes.get(filmId).add(userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        log.debug("Пользователю {} больше не нравится фильм {}", userId, filmId);
        likes.get(filmId).remove(userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        log.debug("Запрос {} самых популярных фильмов", count);
        return  likes.entrySet().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getValue().size(), o1.getValue().size()))
                .map(Map.Entry::getKey)
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList()).stream()
                .mapToInt(number -> number)
                .mapToObj(films::get)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Integer id) {
        log.debug("Запрос фильма под номером {}", id);
        return films.get(id);
    }
}
