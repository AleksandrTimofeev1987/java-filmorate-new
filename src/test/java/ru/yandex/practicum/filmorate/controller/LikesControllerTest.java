package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LikesControllerTest {

    private static final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private static final Film VALID_FILM = new Film(1, "film", RandomString.make(200), RELEASE_DATE, 1L);
    private static final LocalDate BIRTHDAY = LocalDate.now().minusYears(20);
    private static final User VALID_USER = new User(1, "1@mail.ru", "login", "name", BIRTHDAY);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Проверка постановки лайка фильму
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andFilmOnPutLike() throws Exception {
        //given
        postValidFilm();
        postValidUser();

        // when
        mockMvc.perform(
                        put("/films/1/like/1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.likes.length()").value(1))
                .andExpect(jsonPath("$.likes[0]").value(1))
                .andExpect(jsonPath("$.rate").value(1));
    }

    // Проверка удаления лайка фильму
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andFilmOnDeleteLike() throws Exception {
        //given
        postValidFilm();
        postValidUser();
        mockMvc.perform(
                put("/films/1/like/1")
        );

        // when
        mockMvc.perform(
                        delete("/films/1/like/1")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.likes.length()").value(0))
                .andExpect(jsonPath("$.rate").value(0));
    }

    // Проверка получения популярных фильмов
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void shouldReturn200andFilmsOnGetPopular() throws Exception {
        //given
        // Создание 3 фильмов
        postValidFilm();
        postValidFilm();
        postValidFilm();

        // Создание 3 пользователей
        postValidUser();
        postValidUser();
        postValidUser();

        // 3 лайка фильма 1
        mockMvc.perform(
                put("/films/1/like/1")
        );
        mockMvc.perform(
                put("/films/1/like/2")
        );
        mockMvc.perform(
                put("/films/1/like/3")
        );

        // 2 лайка фильма 2
        mockMvc.perform(
                put("/films/2/like/1")
        );
        mockMvc.perform(
                put("/films/2/like/2")
        );

        // 1 лайк фильма 1
        mockMvc.perform(
                put("/films/3/like/1")
        );

        // when
        mockMvc.perform(
                        get("/films/popular")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));

        mockMvc.perform(
                        get("/films/popular")
                                .param("count", "2")
                )

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    private void postValidFilm() throws Exception {
        mockMvc.perform(
                post("/films")
                        .content(objectMapper.writeValueAsString(VALID_FILM))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }

    private void postValidUser() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content(objectMapper.writeValueAsString(VALID_USER))
                        .contentType(MediaType.APPLICATION_JSON)
        );
    }
}
