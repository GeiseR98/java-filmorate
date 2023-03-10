package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @BeforeEach
    public void beforeEach() {
        controller = new UserController();
        controller.addUser(User.builder()
                .email("adeptYaPr@new.org")
                        .login("login")
                        .name("name")
                        .birthday(LocalDate.of(1990,6,24))
                        .build());
    }
    @Test
    public void shouldReturnAListOfUsers() {
        assertEquals(1, controller.findAll().size());
    }
    @Test
    public void shouldUseAUsernameInsteadOfANameWithAnEmptyNameValue() {
        controller.addUser(User.builder()
                .email("adeptYaPr@new.org")
                .login("login")
                .birthday(LocalDate.of(1990,6,24))
                .build());
        assertEquals("login", controller.findAll().get(1).getName());
    }
    @Test
    public void shouldReplaceTheUser() {
                controller.changeUser(User.builder()
                        .id(1)
                        .email("adeptYaPr@new.org")
                        .login("new login")
                        .name("new name")
                        .birthday(LocalDate.of(1990,6,24))
            .build());
        assertEquals(1, controller.findAll().size());
        assertEquals("new name", controller.findAll().get(0).getName());
        assertEquals("new login", controller.findAll().get(0).getLogin());
    }
    @Test
    public void shouldGiveAnErrorWhenRequestingANonExistentUser() {
        ValidationException ex = assertThrows (
                ValidationException.class,
                () -> controller.changeUser(User.builder()
                        .id(5)
                        .email("adeptYaPr@new.org")
                        .login("new login")
                        .name("new name")
                        .birthday(LocalDate.of(1990,6,24))
                        .build())
        );
        assertEquals("данный пользователь не обнаружен", ex.getMessage());
    }
}
