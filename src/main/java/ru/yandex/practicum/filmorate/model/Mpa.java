package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class Mpa {
    private int id;
    @NotBlank(message = "название не может быть пустым")
    private String name;
}

