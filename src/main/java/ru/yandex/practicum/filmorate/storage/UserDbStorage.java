package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository("UserDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final ResultDbEditor resultDbEditor;

    public UserDbStorage(JdbcTemplate jdbcTemplate, ResultDbEditor resultDbEditor) {
        this.jdbcTemplate = jdbcTemplate;
        this.resultDbEditor = resultDbEditor;
    }

    @Override
    public List<User> findAll() {
        String sql = "" +
                "SELECT * " +
                "FROM users";

        List<User> result = jdbcTemplate.query(sql, RowMapper::mapRowToUser);

        // TODO: rewrite in one request to DB!!!
        result.forEach(resultDbEditor::setFriends);
        return result;
    }

    @Override
    public User findById(Integer id) {
        final String sql = "" +
                "SELECT * " +
                "FROM users " +
                "WHERE user_id = ?";

        User result = jdbcTemplate.queryForObject(sql, RowMapper::mapRowToUser, id);

        resultDbEditor.setFriends(result);

        return result;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Integer userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();

        return findById(userId);
    }

    @Override
    public User update(User user) {
        final String sql = "" +
                "UPDATE users " +
                "SET email = ?, login = ?, user_name = ?, birthday = ? " +
                "WHERE user_id = ?";

        final Integer userId = user.getId();

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);

        return findById(userId);
    }

    @Override
    public void checkUserExist(Integer id) {
        final String sql = "" +
                "SELECT COUNT(*) AS count " +
                "FROM users " +
                "WHERE user_id = ?";

        int count = jdbcTemplate.queryForObject(sql, RowMapper::rowMapToCount, id);

        if (count != 1) {
            final String message = String.format("User with id = %d is not found.", id);
            log.warn(message);
            throw new NotFoundException(message);
        }
    }
}
