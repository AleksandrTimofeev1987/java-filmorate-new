package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@Valid
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Film extends StorageData {

    @NotBlank(message = "{name.film.not_blank}")
    private String name;

    @Size(max = 200, message = "{description.film.size}")
    @NotBlank(message = "{description.film.not_blank}")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past(message = "{release.film.not_future}")
    private LocalDate releaseDate;

    @NotNull(message = "{duration.film.not_null}")
    @Positive(message = "{duration.film.positive}")
    private Long duration;

    @NotNull(message = "{rate.film.not_null}")
    private Integer rate = 0;

    @NotNull
    private final Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

    @NotNull
    private MPA mpa;

    private final Set<Integer> likes = new HashSet<>();

    public Film(Integer id, String name, String description, LocalDate releaseDate, Long duration) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
