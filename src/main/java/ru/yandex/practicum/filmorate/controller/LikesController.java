package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.LikesService;

import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
@Validated
public class LikesController {

    private final LikesService likesService;

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Film result = likesService.likeFilm(id, userId);

        log.debug("User with id = {} liked film with id = {}.", userId, id);
        return result;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film dislikeFilm(@PathVariable Integer id, @PathVariable Integer userId) {
        Film result = likesService.dislikeFilm(id, userId);

        log.debug("User with id = {} disliked film with id = {}.", userId, id);
        return result;
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10", required = false) Integer count) {
        validateCountParam(count);

        List<Film> result = likesService.getPopular(count);

        log.debug("Received a list of most popular films in amount of {}.", result.size());
        return result;
    }

    private void validateCountParam(Integer count) {
        if (count <= 0) {
            final String message = "Incorrect count parameter. The parameter should be greater than zero.";
            log.warn(message);
            throw new BadRequestException(message);
        }

    }
}
