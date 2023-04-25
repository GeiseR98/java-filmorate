package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class MpaMapper implements RowMapper<Mpa> {
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("Мапперуем строку в обьект MPA");
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();
    }
}