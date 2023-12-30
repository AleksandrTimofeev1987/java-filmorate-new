package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikesService {

    /**
     * Method put like to film with id by user with userId.
     *
     * @param id Id of film to be liked.
     * @param userId Id of user liking the film.
     *
     * @return Liked film.
     */
    Film likeFilm(Integer id, Integer userId);

    /**
     * Method dislikes film with id by user with userId.
     *
     * @param id Id of film to be disliked.
     * @param userId Id of user disliking the film.
     *
     * @return Disliked film.
     */
    Film dislikeFilm(Integer id, Integer userId);

    /**
     * Method returns most popular films in amount = count.
     *
     * @param count Number of films to be returned.
     *
     * @return Most popular films.
     */
    List<Film> getPopular(Integer count);
}
