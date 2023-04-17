package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genre";
        List<Genre> result = new ArrayList<>(jdbcTemplate.query(sqlQuery, genreMapper));
        log.debug("Предоставляется список жанров: {} наименований", result.size());
        return result;
    }

    @Override
    public Genre getById(Integer genreId) {
        isGenrePresent(genreId);
        String sqlQuery = "SELECT * FROM genre WHERE genre_id = ?";
        log.debug("Запрос жанра под номером {}", genreId);
        return jdbcTemplate.queryForObject(sqlQuery, genreMapper, genreId);
    }

    @Override
    public boolean isGenrePresent(Integer id) {
        String query = "SELECT COUNT (*) FROM genre WHERE genre_id = ?";
        if (jdbcTemplate.queryForObject(query, Integer.class, id) == 0) {
            log.debug(String.format("Жанр фильма под номером %s не найден", id));
            throw new MpaNotFoundException(String.format("Жанр фильма под номером %s не найден", id));
        }
        return true;
    }
}
