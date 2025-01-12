package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.user.Gender;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;

import java.util.List;

public class MockUserDataProvider implements DataProvider<User> {
    private final List<User> users;

    public MockUserDataProvider() {
        this.users = List.of(
                new User.UserBuilder("user1", "password1")
                        .withEmail("user1@gmail.com")
                        .withAge(20)
                        .withGender(Gender.MALE)
                        .subscribe()
                        .build(),
                new User.UserBuilder("user2", "password2")
                        .withEmail("user2@gmail.com")
                        .withAge(25)
                        .withGender(Gender.FEMALE)
                        .build(),
                new User.UserBuilder("user3", "password3")
                        .withEmail("user3@gmail.com")
                        .withAge(30)
                        .withGender(Gender.MALE)
                        .build()
        );
    }

    @Override
    public List<User> fetchData() {
        return users;
    }
}
