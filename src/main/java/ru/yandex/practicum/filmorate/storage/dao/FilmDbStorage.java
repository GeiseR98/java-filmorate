package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }
    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.*, m.mpa_name, g.genre_id, g.genre_name " +
                "FROM films AS f " +
                //"LEFT JOIN film_MPA AS f_m ON f.film_id = f_m.film_id " +
                "JOIN MPA AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genre AS f_g ON f.film_id = f_g.film_id " +
                "LEFT JOIN genre AS g ON f_g.genre_id = g.genre_id";
        return outputtingListFilm(sqlQuery);
    }

    private List<Film> outputtingListFilm(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                Film film = filmMapper.mapRow(rs, filmId);
                films.put(filmId, film);
            }
            String genres_name = rs.getString("genre_name");
            if (genres_name != null) {
                films.get(filmId).addFilmGenre(Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(genres_name).build());
            }
        });
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId((Integer) keyHolder.getKey());
        }
        addGenre(film);
        return getFilmById(film.getId());
    }
    private void addGenre(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
        Set<Genre> genresSet = film.getGenres();
        String addGenresQuery = "MERGE INTO film_genre (film_id, genre_id) " +
                "VALUES (?,?)";
        jdbcTemplate.batchUpdate(addGenresQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                Iterator<Genre> genresIterator = genresSet.iterator();
                for (int j = 0; j <= i && genresIterator.hasNext(); j++) {
                    Genre genre = genresIterator.next();
                    if (j == i) {
                        ps.setInt(2, genre.getId());
                    }
                }
            }
            @Override
            public int getBatchSize() {
                return genresSet.size();
            }
        });
    }

    @Override
    public Film changeFilm(Film film) {
        return null;
    }

    @Override
    public void removeFilmById(Integer id) {

    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {

    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        return null;
    }

    @Override
    public boolean isFilmPresent(Integer id) {
        return false;
    }
}
