package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.ForbiddenException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository("FriendDbStorage")
@Slf4j
public class FriendDbStorage implements FriendStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    public FriendDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public List<User> addFriend(Integer id, Integer friendId) {

        String sql = "" +
                "INSERT INTO user_friends(user_id, friend_id) " +
                "VALUES (?, ?)";

        jdbcTemplate.update(sql, id, friendId);

        List<User> result = new ArrayList<>();
        result.add(userStorage.findById(id));
        result.add(userStorage.findById(friendId));

        return result;
    }

    @Override
    public List<User> deleteFriend(Integer id, Integer friendId) {

        String sql = "" +
                "DELETE FROM user_friends " +
                "WHERE user_id = ? AND friend_id = ?";

        jdbcTemplate.update(sql, id, friendId);

        List<User> result = new ArrayList<>();
        result.add(userStorage.findById(id));
        result.add(userStorage.findById(friendId));

        return result;
    }

    @Override
    public List<User> getFriends(Integer id) {

        String sql = "" +
                "SELECT  uf.friend_id, u.user_id, u.email, u.login, u.user_name, u.birthday " +
                "FROM user_friends AS uf " +
                "LEFT JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = ? " +
                "ORDER BY u.user_id";

        return jdbcTemplate.query(sql, RowMapper::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {

        String sql = "" +
                "SELECT uf.friend_id, u.user_id, u.email, u.login, u.user_name, u.birthday " +
                "FROM user_friends AS uf " +
                "LEFT JOIN users AS u ON uf.friend_id = u.user_id " +
                "WHERE uf.user_id = ? AND uf.friend_id IN (SELECT friend_id " +
                "FROM user_friends " +
                "WHERE user_id = ?)";

        return jdbcTemplate.query(sql, RowMapper::mapRowToUser, id, otherId);
    }

    @Override
    public void checkUsersAreNotFriends(Integer id, Integer friendId) {

        String sql = "" +
                "SELECT COUNT(*) AS count " +
                "FROM user_friends " +
                "WHERE user_id = ? AND friend_id = ?";

        int count = jdbcTemplate.queryForObject(sql, RowMapper::rowMapToCount, id, friendId);

        if (count > 0) {
            final String message = String.format("User with id = %d is already friend with user with id = %d.", id, friendId);
            log.warn(message);
            throw new ForbiddenException(message);
        }
    }


}
