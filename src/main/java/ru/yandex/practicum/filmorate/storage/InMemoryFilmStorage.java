package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryFilmStorage  implements FilmStorage {

    private Integer globalId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findById(Integer id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        film.setId(globalId);
        films.put(globalId, film);
        globalId++;

        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film likeFilm(Integer id, Integer userId) {
        Film film = films.get(id);

        film.getLikes().add(userId);
        film.setRate(film.getRate() + 1);

        return films.get(id);
    }

    @Override
    public Film dislikeFilm(Integer id, Integer userId) {
        Film film = films.get(id);

        film.getLikes().remove(userId);
        film.setRate(film.getRate() - 1);

        return films.get(id);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return films.values()
                .stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())

                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void checkFilmExist(Integer id) {
        if (findById(id) == null) {
            final String message = String.format("Film with id = %d is not found.", id);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }
}
