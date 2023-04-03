package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {
    private FilmController controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void beforeEach() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
        controller.addFilm(Film.builder().name("Don't Be a Menace to South Central While Drinking Your Juice in the Hood").description("описание слишком долгое, что бы его писать сюда").releaseDate(LocalDate.parse("1996-01-12", formatter)).duration((long) 89).build());
    }

    @Test
    public void shouldReturnAListOfMovies() {
        assertEquals(1, controller.findAll().size());
    }

    @Test
    public void shouldReplaceTheMovie() {
        controller.changeFilm(Film.builder().id(1).name("Don't Be a Menace to South Central While Drinking Your Juice in the Hood").description("Фильм о том как черные выживают в гетто").releaseDate(LocalDate.parse("1996-01-12", formatter)).duration((long) 89).build());
        assertEquals("Фильм о том как черные выживают в гетто", controller.findAll().get(0).getDescription());
    }

    @Test
    public void shouldGiveAnErrorWhenRequestingANonExistentMovie() {
        FilmNotFoundException ex = assertThrows(FilmNotFoundException.class, () -> controller.changeFilm(Film.builder().id(3).name("Don't Be a Menace to South Central While Drinking Your Juice in the Hood").description("Фильм о том как черные выживают в гетто").releaseDate(LocalDate.parse("1996-01-12", formatter)).duration((long) 89).build()));
        assertEquals(String.format("Фильм с номером %s не найден", 3), ex.getMessage());
    }
}
