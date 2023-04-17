package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.mappers.MpaMapper;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaMapper mpaMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapper = mpaMapper;
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM mpa";
        List<Mpa> result = new ArrayList<>(jdbcTemplate.query(sqlQuery, mpaMapper));
        log.debug("Текущей размер списка возрастных ограничений: {}", result.size());
        return result;
    }

    @Override
    public Mpa getById(Integer rateId) {
        isMpaPresent(rateId);
        String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
        log.debug("Запрос возрастного ограничения под номером {}", rateId);
        return jdbcTemplate.queryForObject(sqlQuery, mpaMapper, rateId);
    }

    @Override
    public boolean isMpaPresent(Integer id) {
        String query = "SELECT COUNT (*) FROM MPA WHERE mpa_id = ?";
        if (jdbcTemplate.queryForObject(query, Integer.class, id) == 0) {
            log.debug(String.format("Возратное ограничение с номером %s не найдено", id));
            throw new MpaNotFoundException(String.format("Возратное ограничение с номером %s не найдено", id));
        }
        return true;
    }
}
