package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.contents.Content;
import com.github.lorenzoyang.streamingplatform.contents.MockContent;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidUserException;
import com.github.lorenzoyang.streamingplatform.utils.Gender;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class UserTest {
    private StreamingPlatform platform;
    private User registeredUser;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", new MockContentProvider());
        this.registeredUser = new User.UserBuilder("username", "password").build();
        this.platform.registerUser(registeredUser);
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
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Username cannot be null or contain spaces");

        assertThatThrownBy(() -> new User.UserBuilder("username with spaces", "password"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Username cannot be null or contain spaces");
    }

    @Test
    public void testUserBuilderThrowsUserValidationExceptionForInvalidPassword() {
        assertThatThrownBy(() -> new User.UserBuilder("username", null))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Password cannot be null or contain spaces");

        assertThatThrownBy(() -> new User.UserBuilder("username", "password with spaces"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("Password cannot be null or contain spaces");
    }

    @Test
    public void testWithEmailThrowsUserValidationExceptionForInvalidEmail() {
        var builder = new User.UserBuilder("username", "password");
        var msg = "Email must be a valid format containing '@' and '.'";

        assertThatThrownBy(() -> builder.withEmail(null))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email@"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withEmail("email.com"))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage(msg);
    }

    @Test
    public void testWithAgeThrowsUserValidationExceptionForInvalidAge() {
        var builder = new User.UserBuilder("username", "password");
        var msg = "Age must be between 13 and 150.";

        assertThatThrownBy(() -> builder.withAge(12))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage(msg);

        assertThatThrownBy(() -> builder.withAge(151))
                .isInstanceOf(InvalidUserException.class)
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
        Content mockContent1 = new MockContent.MockContentBuilder("Mock Content 1").build();
        user.watch(mockContent1, 60);

        assertThat(user.getToWatchList()).containsExactly(mockContent1);
        assertThat(user.getWatchedList()).isEmpty();

        Content mockContent2 = new MockContent.MockContentBuilder("Mock Content 2").build();
        user.getWatchedList().add(mockContent2);
        user.watch(mockContent2, 60);

        assertThat(user.getToWatchList())
                .hasSize(2)
                .contains(mockContent1, mockContent2);
        assertThat(user.getWatchedList()).isEmpty();
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
        Content toWatchContent = platform.getContents().get(0);
        Content watchedContent = platform.getContents().get(1);

//        registeredUser.getToWatchList().put(toWatchContent, ViewingProgress.empty());
//        registeredUser.getWatchedList().add(watchedContent);
//
//        platform.removeContent(toWatchContent);
//        platform.removeContent(watchedContent);
//
//        assertThat(registeredUser.getToWatchList()).isEmpty();
//        assertThat(registeredUser.getWatchedList()).isEmpty();
    }

    @Test
    public void testUpdateForUpdateContentEvent() {
        Content mockToWatchContent = new MockContent.MockContentBuilder("toWatchContent").build();
        Content mockWatchedContent = new MockContent.MockContentBuilder("watchedContent").build();
        platform.addContent(mockToWatchContent);
        platform.addContent(mockWatchedContent);

//        registeredUser.getToWatchList().put(mockToWatchContent, ViewingProgress.empty());
//        registeredUser.getWatchedList().add(mockWatchedContent);
//
//        Content mockNewToWatchContent = new MockContent.MockContentBuilder(mockToWatchContent.getTitle())
//                .withDescription("New description")
//                .build();
//        platform.updateContent(mockNewToWatchContent);
//
//        assertThat(registeredUser.getToWatchList()).containsOnlyKeys(mockNewToWatchContent);
//        Content updatedToWatchContent = registeredUser.getToWatchList().keySet().iterator().next();
//        assertThat(updatedToWatchContent.getDescription()).isEqualTo("New description");
//
//        Content mockNewWatchedContent = new MockContent.MockContentBuilder(mockWatchedContent.getTitle())
//                .withDescription("New description")
//                .build();
//        platform.updateContent(mockNewWatchedContent);
//
//        assertThat(registeredUser.getWatchedList()).containsOnly(mockNewWatchedContent);
//        Content updatedWatchedContent = registeredUser.getWatchedList().iterator().next();
//        assertThat(updatedWatchedContent.getDescription()).isEqualTo("New description");
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