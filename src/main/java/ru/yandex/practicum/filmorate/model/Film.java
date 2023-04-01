package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private final String name;
    @Size(min = 1, max = 200, message = "максимальная длина описания — 200 символов")
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private final Long duration;
}
