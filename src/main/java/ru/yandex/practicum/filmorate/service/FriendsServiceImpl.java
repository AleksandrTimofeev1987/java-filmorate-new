package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FriendsServiceImpl implements FriendsService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public FriendsServiceImpl(@Qualifier("UserDbStorage") UserStorage userStorage, @Qualifier("FriendDbStorage")FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    @Override
    public List<User> addFriend(Integer id, Integer friendId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(friendId);

        friendStorage.checkUsersAreNotFriends(id, friendId);

        return friendStorage.addFriend(id, friendId);
    }

    @Override
    public List<User> deleteFriend(Integer id, Integer friendId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(friendId);

        return friendStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        userStorage.checkUserExist(id);

        return friendStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userStorage.checkUserExist(id);
        userStorage.checkUserExist(otherId);

        return friendStorage.getCommonFriends(id, otherId);
    }
}
