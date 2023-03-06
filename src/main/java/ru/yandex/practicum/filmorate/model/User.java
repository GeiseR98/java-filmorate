package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User extends Model{
    @NonNull
    private final int id;
    @Email(message = "электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotBlank(message = "логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past(message = "дата рождения не может быть в будущем")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
