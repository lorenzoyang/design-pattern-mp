package com.github.lorenzoyang.streamingplatform.user;

import com.github.lorenzoyang.streamingplatform.exceptions.UserValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTest {

    @Test
    public void testUserBuilderCreatesUserWithValidData() {
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
    public void testUserBuilderThrowsUserValidationExceptionForInvalidUsername() {
        assertThatThrownBy(() -> new User.UserBuilder(null, "password"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Username must be between 3 and 30 characters.");

        assertThatThrownBy(() -> new User.UserBuilder("ab", "password"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Username must be between 3 and 30 characters.");

        assertThatThrownBy(() -> new User.UserBuilder("a".repeat(31), "password"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Username must be between 3 and 30 characters.");
    }

    @Test
    public void testUserBuilderThrowsUserValidationExceptionForInvalidPassword() {
        assertThatThrownBy(() -> new User.UserBuilder("username", null))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Password must be between 8 and 30 characters.");

        assertThatThrownBy(() -> new User.UserBuilder("username", "1234567"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Password must be between 8 and 30 characters.");

        assertThatThrownBy(() -> new User.UserBuilder("username", "1".repeat(31)))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Password must be between 8 and 30 characters.");
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
                .hasMessage("Email must be a valid format containing '@' and '.'");

        assertThatThrownBy(() -> builder.withEmail("email@"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Email must be a valid format containing '@' and '.'");

        assertThatThrownBy(() -> builder.withEmail("email.com"))
                .isInstanceOf(UserValidationException.class)
                .hasMessage("Email must be a valid format containing '@' and '.'");
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
    public void testSubscribeSetsHasSubscriptionToTrue() {
        User user = new User.UserBuilder("username", "password")
                .subscribe()
                .build();
        assertThat(user.hasSubscription()).isTrue();
    }

    @Test
    public void testWatchRunsCorrectly() {

    }

    @Test
    public void testWatchThrowsNullPointerExceptionForNullContent() {
        User user = new User.UserBuilder("username", "password").build();
        assertThatThrownBy(() -> user.watch(null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
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