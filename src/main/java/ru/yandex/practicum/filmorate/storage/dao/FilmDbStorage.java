package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
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
@Slf4j
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
        String sqlQuery = "SELECT f.*, g.genre_id, g.genre_name, m.mpa_id, m.mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN film_genre AS f_g ON f.film_id = f_g.film_id " +
                "LEFT JOIN genre AS g ON f_g.genre_id = g.genre_id " +
                "LEFT JOIN film_mpa AS f_m ON f.film_id = f_m.film_id " +
                "LEFT JOIN mpa AS m ON f_m.mpa_id = m.mpa_id";
        return outputtingListFilm(sqlQuery);
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlQuery = "SELECT f.*, g.genre_id, g.genre_name, m.mpa_id, m.mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN film_genre AS f_g ON f.film_id = f_g.film_id " +
                "LEFT JOIN genre AS g ON f_g.genre_id = g.genre_id " +
                "LEFT JOIN film_mpa AS f_m ON f.film_id = f_m.film_id " +
                "LEFT JOIN mpa AS m ON f_m.mpa_id = m.mpa_id " +
                "WHERE f.film_id = " + id;

        List<Film> films = outputtingListFilm(sqlQuery);
        if (!films.isEmpty()) {
            return films.get(0);
        } else {
            log.debug("Фильм с номером {} не найден", id);
            throw new FilmNotFoundException(String.format("Фильм с номером %s не найден", id));
        }
    }

    private List<Film> outputtingListFilm(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                Film film = filmMapper.mapRow(rs, filmId);
                films.put(filmId, film);
            }
            String genresName = rs.getString("genre_name");
            if (genresName != null) {
                films.get(filmId).addFilmGenre(Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(genresName)
                        .build());
            }
            String mpaName = rs.getString("mpa_name");
            int mpaId = rs.getInt("mpa_id");
                films.get(filmId).setMpa(Mpa.builder()
                        .id(mpaId)
                        .name(mpaName)
                        .build());
        });
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration) " +
                "VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId((Integer) keyHolder.getKey());
        }
        addGenre(film);
        addMpa(film);
        return getFilmById(film.getId());
    }

    private void addMpa(Film film) {
        Integer filmId = film.getId();
        Integer mpaId = film.getMpa().getId();
        jdbcTemplate.update("DELETE FROM film_mpa WHERE film_id = ?", filmId);
        String sqlQuery = "INSERT INTO film_mpa (film_id, mpa_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, mpaId);
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
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getId());
        addGenre(film);
        addMpa(film);
        return getFilmById(film.getId());
    }

    @Override
    public void removeFilmById(Integer id) {
        String sqlQuery = "DELETE FROM films WHERE films_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, g.genre_id, g.genre_name, m.mpa_id, m.mpa_name " +
                "FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS likes " +
                "                FROM likes GROUP BY film_id) AS  l ON f.film_id = l.film_id " +
                "LEFT JOIN film_genre AS f_g ON f.film_id = f_g.film_id " +
                "                LEFT JOIN genre AS g ON f_g.genre_id = g.genre_id " +
                "                LEFT JOIN film_mpa AS f_m ON f.film_id = f_m.film_id" +
                "                LEFT JOIN mpa AS m ON f_m.mpa_id = m.mpa_id " +
                "ORDER BY likes DESC " +
                "LIMIT " + count;

        return outputtingListFilm(sqlQuery);
    }



    @Override
    public boolean isFilmPresent(Integer id) {
        String query = "SELECT COUNT (*) FROM films WHERE film_id = ?";
        if (jdbcTemplate.queryForObject(query, Integer.class, id) == 0) {
            log.debug(String.format("Фильм с номером %s не найден", id));
            throw new UserNotFoundException(String.format("Фильм с номером %s не найден", id));
        }
        return true;
    }
}
