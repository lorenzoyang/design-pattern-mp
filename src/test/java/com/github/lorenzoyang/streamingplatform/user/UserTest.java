package com.github.lorenzoyang.streamingplatform.user;

import com.github.lorenzoyang.streamingplatform.MockContentDataProvider;
import com.github.lorenzoyang.streamingplatform.MockUserDataProvider;
import com.github.lorenzoyang.streamingplatform.StreamingPlatform;
import com.github.lorenzoyang.streamingplatform.content.*;
import com.github.lorenzoyang.streamingplatform.exceptions.UserValidationException;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTest {
    private DataProvider<Content> contentDataProvider;
    private DataProvider<User> userDataProvider;
    private StreamingPlatform platform;

    @Before
    public void setUp() {
        this.contentDataProvider = new MockContentDataProvider();
        this.userDataProvider = new MockUserDataProvider();
        this.platform = new StreamingPlatform("Streaming Platform", contentDataProvider, userDataProvider);
    }

    @Test
    public void testUserBuilderCreatesUserWithValidArguments() {
        User user = new User.UserBuilder("username", "password")
                .withEmail("email@gmail.com")
                .withAge(20)
                .withGender(Gender.MALE)
                .subscribe()
                .build();

        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getPassword()).isEqualTo("password");
        assertThat(user.getEmail()).isEqualTo("email@gmail.com");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.hasSubscription()).isTrue();
    }

    @Test
    public void testUserBuilderConstructorThrowsUserValidationExceptionForInvalidUsername() {
        assertThatThrownBy(() -> new User.UserBuilder(null, "password"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Username cannot be null or contain spaces");

        assertThatThrownBy(() -> new User.UserBuilder("username with spaces", "password"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Username cannot be null or contain spaces");
    }

    @Test
    public void testUserBuilderThrowsUserValidationExceptionForInvalidPassword() {
        assertThatThrownBy(() -> new User.UserBuilder("username", null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Password cannot be null or contain spaces");

        assertThatThrownBy(() -> new User.UserBuilder("username", "password with spaces"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Password cannot be null or contain spaces");
    }

    @Test
    public void testWithEmailThrowsUserValidationExceptionForInvalidEmail() {
        var builder = new User.UserBuilder("username", "password");
        var msg = "Email must be a valid format containing '@' and '.'";

        assertThatThrownBy(() -> builder.withEmail(null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email@"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email.com"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);
    }

    @Test
    public void testWithAgeThrowsUserValidationExceptionForInvalidAge() {
        var builder = new User.UserBuilder("username", "password");
        var msg = "Age must be between 13 and 150.";

        assertThatThrownBy(() -> builder.withAge(12))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withAge(151))
                .isInstanceOf(UserValidationException.class)
                .hasMessage(msg);
    }

    @Test
    public void testWithGenderThrowsNullPointerExceptionForNullGender() {
        var builder = new User.UserBuilder("username", "password");
        assertThatThrownBy(() -> builder.withGender(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Gender cannot be null");
    }

    @Test
    public void testSubscribeRunsCorrectly() {
        User user = new User.UserBuilder("username", "password")
                .subscribe()
                .build();
        assertThat(user.hasSubscription()).isTrue();
    }

    @Test
    public void testWatchRunsCorrectly() {
        User user = new User.UserBuilder("username", "password").build();
        Content content = new Movie.MovieBuilder(
                "movie",
                new Episode("episode", 1, 120)
        ).build();

        user.watch(content, 60);
        assertThat(user.getToWatchList()).hasSize(1);
        assertThat(user.getToWatchList()).containsOnlyKeys(content);

        user.watch(content, 60);
        assertThat(user.getToWatchList()).isEmpty();
        assertThat(user.getWatchedList()).hasSize(1);
        assertThat(user.getWatchedList()).containsExactly(content);
    }

    @Test
    public void testWatchThrowsNullPointerExceptionForNullContent() {
        User user = new User.UserBuilder("username", "password").build();
        assertThatThrownBy(() -> user.watch(null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testUpdateForRemoveContentEvent() {
        User registeredUser = userDataProvider.fetchData().get(0);
        List<Content> contents = contentDataProvider.fetchData();
        registeredUser.getToWatchList().put(contents.get(0), ViewingProgress.empty());
        registeredUser.getWatchedList().add(contents.get(1));

        platform.removeContent(contents.get(0));
        platform.removeContent(contents.get(1));

        assertThat(registeredUser.getToWatchList()).isEmpty();
        assertThat(registeredUser.getWatchedList()).isEmpty();
    }

    @Test
    public void testUpdateForReplaceContentEvent() {
        User registeredUser = userDataProvider.fetchData().get(0);
        Content toWatchTvSeries = contentDataProvider.fetchData().get(0);
        Content watchedTvSeries = contentDataProvider.fetchData().get(1);
        registeredUser.getToWatchList().put(toWatchTvSeries, ViewingProgress.empty());
        registeredUser.getWatchedList().add(watchedTvSeries);

        Content newTvSeries = new TVSeries.TVSeriesBuilder(toWatchTvSeries.getTitle())
                .withDescription("toWatchTvSeries Description")
                .build();
        platform.replaceContent(newTvSeries);
        assertThat(registeredUser.getToWatchList()).containsOnlyKeys(newTvSeries);
        Content updatedKeyOfToWatchList = registeredUser.getToWatchList().keySet().iterator().next();
        assertThat(updatedKeyOfToWatchList.getDescription()).isEqualTo("toWatchTvSeries Description");

        newTvSeries = new TVSeries.TVSeriesBuilder(watchedTvSeries.getTitle())
                .withDescription("watchedTvSeries Description")
                .build();
        platform.replaceContent(newTvSeries);
        assertThat(registeredUser.getWatchedList()).containsOnly(newTvSeries);
        Content updatedKeyOfWatchedList = registeredUser.getWatchedList().iterator().next();
        assertThat(updatedKeyOfWatchedList.getDescription()).isEqualTo("watchedTvSeries Description");
    }

    @Test
    public void testEqualsReturnsTrueForSameUsername() {
        User user1 = new User.UserBuilder("username", "password1").build();
        User user2 = new User.UserBuilder("username", "password2").build();
        assertThat(user1.equals(user2)).isTrue();
    }

    @Test
    public void testEqualsReturnsFalseForDifferentUsername() {
        User user1 = new User.UserBuilder("username1", "password").build();
        User user2 = new User.UserBuilder("username2", "password").build();
        assertThat(user1.equals(user2)).isFalse();
    }

    @Test
    public void testHashCodeIsBeBasedOnUsername() {
        User user1 = new User.UserBuilder("username", "password1").build();
        User user2 = new User.UserBuilder("username", "password2").build();
        User user3 = new User.UserBuilder("username3", "password3").build();

        assertThat(user1.hashCode()).isEqualTo(user2.hashCode());
        assertThat(user1.hashCode()).isNotEqualTo(user3.hashCode());
        assertThat(user2.hashCode()).isNotEqualTo(user3.hashCode());
    }
}