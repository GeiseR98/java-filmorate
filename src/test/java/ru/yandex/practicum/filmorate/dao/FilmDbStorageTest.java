package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FilmDbStorageTest {
    final FilmDbStorage filmDbStorage;
    final UserDbStorage userDbStorage;

    @Test
    void shouldAddFilmInBd() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 1")
                .description("описание тестового фильма 1")
                .releaseDate(LocalDate.of(1988, 11, 8))
                .duration(107)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film addFilm = filmDbStorage.addFilm(film);
        assertEquals(addFilm.getName(), film.getName());
        assertEquals("G", addFilm.getMpa().getName());
        Assertions.assertTrue(addFilm.getGenres().contains(genre));
    }

    @Test
    void shouldChangeFilmInBd() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 2")
                .description("описание тестового фильма 2")
                .releaseDate(LocalDate.of(1988, 11, 8))
                .duration(120)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film changeFilm = filmDbStorage.addFilm(film);
        Mpa changeRate = Mpa.builder().id(2).build();
        Genre changeGenre = Genre.builder().id(3).build();
        changeFilm.setName("новый тестовый фильм 2");
        changeFilm.setDescription("новое описание тестового фильма 2");
        changeFilm.setReleaseDate(LocalDate.of(1993, 6, 14));
        changeFilm.setDuration(102);
        changeFilm.setMpa(changeRate);
        changeFilm.addFilmGenre(changeGenre);

        Film updatedFilm = filmDbStorage.changeFilm(changeFilm);

        assertEquals(updatedFilm.getName(), changeFilm.getName());
        assertEquals("PG", updatedFilm.getMpa().getName());
        Assertions.assertTrue(updatedFilm.getGenres().contains(changeGenre));
    }

    @Test
    void shouldGetFilmById() {
        Mpa mpa = Mpa.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("тестовый фильм 3")
                .description("описание тестового фильма 3")
                .releaseDate(LocalDate.of(1950, 4, 13))
                .duration(30)
                .mpa(mpa)
                .build();
        film.addFilmGenre(genre);

        Film addFilm = filmDbStorage.addFilm(film);
        Film getFilm = filmDbStorage.getFilmById(addFilm.getId());

        assertEquals(getFilm.getName(), film.getName());
        assertEquals("G", getFilm.getMpa().getName());
        Assertions.assertTrue(getFilm.getGenres().contains(genre));
    }

    @Test
    void shouldAddLikeAndGetPopularFilm() {
        User user = User.builder()
                .email("FatalR@yandex.ru")
                .login("FatalR")
                .name("Sergey")
                .birthday(LocalDate.of(1993, 6, 14))
                .build();

        Mpa mpa = Mpa.builder().id(3).build();
        Genre genre = Genre.builder().id(6).build();
        Film film = Film.builder()
                .name("тестовый фильм 5")
                .description("описание тестового фильма 5")
                .releaseDate(LocalDate.of(2022, 12, 31))
                .duration(120)
                .mpa(mpa)
                .build();

        film.addFilmGenre(genre);
        Film addFilm = filmDbStorage.addFilm(film);
        User likeUser = userDbStorage.createUser(user);
        filmDbStorage.addLike(addFilm.getId(), likeUser.getId());

        List<Film> listFilm = filmDbStorage.getPopularFilms(1);
        assertEquals(listFilm.get(0).getName(), addFilm.getName());
    }
}