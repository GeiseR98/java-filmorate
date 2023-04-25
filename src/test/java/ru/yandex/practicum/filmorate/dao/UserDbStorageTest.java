package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    void shouldCreateUserInBd() {
        User user1 = User.builder()
                .email("test@test.com")
                .login("TestLogin1")
                .name("TestName1")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        User saveUser = userDbStorage.createUser(user1);
        assertEquals(user1, saveUser);
    }

    @Test
    void shouldChangeUser() {
        User user1 = User.builder()
                .email("test2@test.com")
                .login("TestLogin2")
                .name("TestName2")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        final User saveUser = userDbStorage.createUser(user1);
        saveUser.setEmail("test3@test.com");
        saveUser.setLogin("TestLogin3");
        saveUser.setName("TestName3");
        saveUser.setBirthday(LocalDate.of(1997, 9, 9));

        final User changedUser = userDbStorage.changeUser(saveUser);

        assertEquals(saveUser, changedUser);
    }

    @Test
    void shouldGetUserById() {
        User user1 = User.builder()
                .email("test4@test.com")
                .login("TeslLogin4")
                .name("TestName4")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();

        Integer userId = userDbStorage.createUser(user1).getId();
        User testUser = userDbStorage.getUserById(userId);

        assertEquals(user1, testUser);
        userDbStorage.removeUsers(user1.getId());
    }

    @Test
    void shouldSaveFriendAndGetFriend() {
        User oneUser = User.builder()
                .email("test5@test.com")
                .login("TeslLogin5")
                .name("TestName5")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
        User twoUser = User.builder()
                .email("test6@test.com")
                .login("TeslLogin6")
                .name("TestName6")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();

        Integer userId = userDbStorage.createUser(oneUser).getId();
        Integer friendUserId = userDbStorage.createUser(twoUser).getId();

        userDbStorage.addAsFriends(userId, friendUserId);

        List<User> testFriend = userDbStorage.getFriends(userId);

        assertEquals(1, testFriend.size());
        assertEquals(testFriend.get(0).getEmail(), twoUser.getEmail());
        assertEquals(testFriend.get(0).getBirthday(), twoUser.getBirthday());
    }

    @Test
    void shouldDeleteFriend() {
        User user1 = User.builder()
                .email("test7@test.com")
                .login("TeslLogin7")
                .name("TestName7")
                .birthday(LocalDate.of(1999, 9, 9))
                .build();
        User friendUser = User.builder()
                .email("test8@test.com")
                .login("TeslLogin7")
                .name("TestName67")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();

        Integer userId = userDbStorage.createUser(user1).getId();
        Integer friendUserId = userDbStorage.createUser(friendUser).getId();

        userDbStorage.addAsFriends(userId, friendUserId);
        userDbStorage.removeFromFriends(userId, friendUserId);

        List<User> testFriend = userDbStorage.getFriends(userId);

        assertEquals(0, testFriend.size());
    }

    @Test
    void shouldGetCorporateFriends() {
        User user1 = User.builder()
                .email("test9@test.com")
                .login("TeslLogin9")
                .name("TestName9")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        User user2 = User.builder()
                .email("test10@test.com")
                .login("TeslLogin10")
                .name("TestName10")
                .birthday(LocalDate.of(1991, 9, 9))
                .build();
        User user3 = User.builder()
                .email("test11@test.com")
                .login("TeslLogin11")
                .name("TestName11")
                .birthday(LocalDate.of(1992, 9, 9))
                .build();

        Integer user1Id = userDbStorage.createUser(user1).getId();
        Integer user2Id = userDbStorage.createUser(user2).getId();
        Integer user3Id = userDbStorage.createUser(user3).getId();

        userDbStorage.addAsFriends(user1Id, user3Id);
        userDbStorage.addAsFriends(user2Id, user3Id);

        List<User> commonFriends = userDbStorage.getListOfMutualFriends(user1Id, user2Id);

        assertEquals(1, commonFriends.size());
        assertEquals(commonFriends.get(0).getEmail(), user3.getEmail());
        assertEquals(commonFriends.get(0).getBirthday(), user3.getBirthday());
    }

}