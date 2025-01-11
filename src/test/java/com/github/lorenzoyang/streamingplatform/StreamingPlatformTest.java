package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StreamingPlatformTest {
    private DataProvider<User> userDataProvider;
    private DataProvider<Content> contentDataProvider;
    private StreamingPlatform streamingPlatform;

    @Before
    public void setUp() {
        this.userDataProvider = new MockUserDataProvider();
        this.contentDataProvider = new MockContentDataProvider();
        this.streamingPlatform = new StreamingPlatform("Streaming Platform", contentDataProvider, userDataProvider);
    }

    @Test
    public void testAttachAndDetachRunCorrectly() {
        PlatformObserver observer = (event) -> {
            // Just for testing
        };
        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size());
        streamingPlatform.attach(observer);
        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size() + 1);
        streamingPlatform.detach(observer);
        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size());
    }

    @Test
    public void testRegisterUserThrowsNullPointerExceptionForNullUser() {
        assertThatThrownBy(() -> streamingPlatform.registerUser(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");
    }

    @Test
    public void testRegisterUserThrowsIllegalArgumentExceptionForExistingUser() {
        User user = userDataProvider.fetchData().get(0);
        assertThatThrownBy(() -> streamingPlatform.registerUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already registered");
    }

    @Test
    public void testRegisterUserRunsCorrectly() {
        User user = new User.UserBuilder("user4", "password4")
                .build();
        streamingPlatform.registerUser(user);
        assertThat(streamingPlatform.getUsers())
                .toIterable()
                .contains(user);
    }

    @Test
    public void testUnregisterUserThrowsIllegalArgumentExceptionForNonExistingUser() {
        User user = new User.UserBuilder("user4", "password4")
                .build();
        assertThatThrownBy(() -> streamingPlatform.unregisterUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");
    }
}