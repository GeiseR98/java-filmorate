package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class FilmMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.debug("/mapRowUser");
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(Mpa.builder()
                        .mpaId(rs.getInt("mpa_id"))
                        .mpaName(rs.getString("mpa_name"))
                        .build())
                .build();

        if (rs.getInt("genre_id") != 0) {
            Genre genre = Genre.builder()
                    .genreId(rs.getInt("genre_id"))
                    .genreName(rs.getString("genre_name"))
                    .build();
            film.addFilmGenre(genre);
        }
        return film;
    }
}