package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private Integer id;
    private LocalDate releaseDate;

    @NotBlank
    private final String name;

    @Size(min = 0, max = 200)
    private final String description;

    @Positive
    private final int duration;
}
