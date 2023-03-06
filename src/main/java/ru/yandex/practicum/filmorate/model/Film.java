package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class Film extends Model{
    @NonNull
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private final String name;
    @Size(min = 1, max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @JsonFormat(pattern = "dd.MM.yyyy")
    private final LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private final Duration duration;
}
