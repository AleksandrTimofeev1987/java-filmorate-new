package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class LikesServiceImpl implements LikesService{

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    public LikesServiceImpl(@Qualifier("UserDbStorage") UserStorage userStorage, @Qualifier("FilmDbStorage") FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public Film likeFilm(Integer id, Integer userId) {
        filmStorage.checkFilmExist(id);
        userStorage.checkUserExist(userId);

        return filmStorage.likeFilm(id, userId);
    }

    @Override
    public Film dislikeFilm(Integer id, Integer userId) {
        filmStorage.checkFilmExist(id);
        userStorage.checkUserExist(userId);

        checkUserLikedFilm(id, userId);

        return filmStorage.dislikeFilm(id, userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }

    private void checkUserLikedFilm(Integer id, Integer userId) {
        if (!filmStorage.findById(id).getLikes().contains(userId)) {
            final String message = String.format("Dislike is forbidden. User with id = %d have not previously liked film with id = %d.", userId, id);
            log.warn(message);
            throw new NotFoundException(message);
        }

    }
}
