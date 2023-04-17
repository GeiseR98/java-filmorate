package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.GenreDbStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void shouldGetAll() {
        List<Genre> genres = genreDbStorage.getAll();
        assertEquals(6, genres.size());
    }

    @Test
    void shouldGetById() {
        Genre testGenre = genreDbStorage.getById(2);
        assertEquals("Драма", testGenre.getName());
    }
}