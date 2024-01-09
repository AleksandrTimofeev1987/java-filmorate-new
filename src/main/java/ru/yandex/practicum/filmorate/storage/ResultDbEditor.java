package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashSet;
import java.util.Set;

@Repository("ResultDbEditor")
public class ResultDbEditor {

    private final JdbcTemplate jdbcTemplate;

    public ResultDbEditor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setFriends(User user) {
        Integer userId = user.getId();

        String sql = "" +
                "SELECT friend_id " +
                "FROM user_friends " +
                "WHERE user_id = ?";

        Set<Integer> friends = new HashSet<>(jdbcTemplate.query(sql, RowMapper::mapRowToFriendIds, userId));

        user.setFriends(friends);
    }
}
