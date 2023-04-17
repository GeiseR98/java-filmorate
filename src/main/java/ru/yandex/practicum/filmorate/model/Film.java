package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validator.ReleaseDateConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class Film {
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
    @Size(min = 1, max = 200, message = "максимальная длина описания — 200 символов")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ReleaseDateConstraint
    private LocalDate releaseDate;
    @Positive(message = "продолжительность фильма должна быть положительной")
    private int duration;
    @NotNull
    private int rate;
    @NotNull
    private Mpa mpa;

    public void addFilmGenre(Genre genre) {
        genres.add(genre);
    }
}