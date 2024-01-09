package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {
    @Override
    public List<Film> findAll() {
        return null;
    }

    @Override
    public Film findById(Integer id) {
        return null;
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film likeFilm(Integer id, Integer userId) {
        return null;
    }

    @Override
    public Film dislikeFilm(Integer id, Integer userId) {
        return null;
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return null;
    }

    @Override
    public void checkFilmExist(Integer id) {

    }
}
