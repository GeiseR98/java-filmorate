package ru.yandex.practicum.filmorate.controllersTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {
    private UserController controller;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @BeforeEach
    public void beforeEach() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
        controller.createUser(User.builder()
                .email("adeptYaPr@new.org")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1990, 6, 24))
                .build());
    }

    @Test
    public void shouldReturnAListOfUsers() {
        assertEquals(1, controller.getAllUsers().size());
    }

    @Test
    public void shouldUseAUsernameInsteadOfANameWithAnEmptyNameValue() {
        controller.createUser(User.builder()
                .email("adeptYaPr@new.org")
                .login("login")
                .birthday(LocalDate.of(1990, 6, 24))
                .build());
        assertEquals("login", controller.getAllUsers().get(1).getName());
    }

    @Test
    public void shouldReplaceTheUser() {
        controller.changeUser(User.builder()
                .id(1)
                .email("adeptYaPr@new.org")
                .login("new login")
                .name("new name")
                .birthday(LocalDate.of(1990, 6, 24))
                .build());
        assertEquals(1, controller.getAllUsers().size());
        assertEquals("new name", controller.getAllUsers().get(0).getName());
        assertEquals("new login", controller.getAllUsers().get(0).getLogin());
    }

    @Test
    public void shouldGiveAnErrorWhenRequestingANonExistentUser() {
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> controller.changeUser(User.builder()
                        .id(5)
                        .email("adeptYaPr@new.org")
                        .login("new login")
                        .name("new name")
                        .birthday(LocalDate.of(1990, 6, 24))
                        .build())
        );
        assertEquals((String.format("Пользователь с идентификатором %s не найден", 5)), ex.getMessage());
    }
}
