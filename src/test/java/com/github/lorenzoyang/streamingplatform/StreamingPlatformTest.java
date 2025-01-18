package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.mocks.MockContent;
import com.github.lorenzoyang.streamingplatform.mocks.MockPlatformObserver;
import com.github.lorenzoyang.streamingplatform.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class StreamingPlatformTest {
    private StreamingPlatform platform;
    private User user;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", Stream::empty);
        this.user = new User.UserBuilder("user", "password").build();
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new StreamingPlatform(null, Stream::empty))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Streaming platform name cannot be null");

        assertThatThrownBy(() -> new StreamingPlatform("Streaming Platform", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content provider cannot be null");
    }

    @Test
    public void testRegisterUserAndUnregisterUserRunCorrectly() {
        boolean isRegistered = platform.registerUser(user);
        assertTrue(isRegistered);
        assertThat(platform.getUsers()).containsExactly(user);

        boolean failedToRegister = platform.registerUser(user);
        assertFalse(failedToRegister);
        assertThat(platform.getUsers()).containsExactly(user);

        boolean isUnregistered = platform.unregisterUser(user);
        assertTrue(isUnregistered);
        assertThat(platform.getUsers()).isEmpty();

        boolean failedToUnregister = platform.unregisterUser(user);
        assertFalse(failedToUnregister);
    }

    @Test
    public void testAddContentAndRemoveContentRunCorrectly() {
        Content mockContent = new MockContent("Mock Content");
        int initialSize = platform.getContents().size();
        boolean isAdded = platform.addContent(mockContent);

        assertTrue(isAdded);
        assertThat(platform.getContents())
                .hasSize(initialSize + 1)
                .contains(mockContent);

        boolean failedToAdd = platform.addContent(mockContent);
        assertFalse(failedToAdd);
        assertThat(platform.getContents()).hasSize(initialSize + 1);

        boolean isRemoved = platform.removeContent(mockContent);
        assertTrue(isRemoved);
        assertThat(platform.getContents())
                .hasSize(initialSize)
                .doesNotContain(mockContent);

        boolean failedToRemove = platform.removeContent(mockContent);
        assertFalse(failedToRemove);
        assertThat(platform.getContents()).hasSize(initialSize);
    }

    @Test
    public void testUpdateContentRunsCorrectly() {
        Content oldContent = new MockContent.MockContentBuilder("Mock Content")
                .withDescription("Old description")
                .build();
        platform.getContents().add(oldContent);
        int initialSize = platform.getContents().size();
        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("Updated description")
                .build();
        boolean isUpdated = platform.updateContent(updatedContent);

        assertTrue(isUpdated);
        assertThat(platform.getContents())
                .hasSize(initialSize)
                .contains(updatedContent);

        Content content = platform.getContents().stream()
                .filter(c -> c.equals(updatedContent))
                .findFirst()
                .orElse(null);

        assertThat(content).isNotNull();
        assertThat(content.getDescription()).contains("Updated description");

        Content mockContent = new MockContent("Not existing content");
        boolean failedToUpdate = platform.updateContent(mockContent);
        assertFalse(failedToUpdate);
    }

    @Test
    public void testWatchContentRunsCorrectly() {
        Content mockContent = new MockContent("Mock Content") {
            @Override
            public int getDurationInMinutes() {
                return 10;
            }

            @Override
            public boolean isFree() {
                return false;
            }
        };
        User subscribedUser = new User.UserBuilder("subscribedUser", "password")
                .subscribe()
                .build();
        platform.getContents().add(mockContent);
        platform.getUsers().add(subscribedUser);

        int timeToWatch = 5;
        int expected = Math.min(timeToWatch, mockContent.getDurationInMinutes());
        assertEquals(expected, platform.watchContent(subscribedUser, mockContent, timeToWatch));

        timeToWatch = 15;
        expected = Math.min(timeToWatch, mockContent.getDurationInMinutes());
        assertEquals(expected, platform.watchContent(subscribedUser, mockContent, timeToWatch));
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNonExistingUser() {
        Content mockContent = new MockContent("Mock Content");
        platform.getContents().add(mockContent);

        assertThatThrownBy(() -> platform.watchContent(user, mockContent, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User 'user' is not registered");
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content mockContent = new MockContent("Mock Content");
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, mockContent, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content 'Mock Content' does not exist");
    }

    @Test
    public void testWatchContentThrowsAccessDeniedExceptionForNonSubscribedUser() {
        Content mockContent = new MockContent("Mock Content") {
            @Override
            public boolean isFree() {
                return false;
            }
        };
        platform.getContents().add(mockContent);
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, mockContent, 10))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("User 'user' does not have a subscription");
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        Content mockContent = new MockContent("Mock Content");
        platform.getContents().add(mockContent);
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, mockContent, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch must be greater than 0");
        assertThatThrownBy(() -> platform.watchContent(user, mockContent, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch must be greater than 0");
    }

    @Test
    public void testAddObserverAndRemoveObserverRunCorrectly() {
        PlatformObserver observer = new MockPlatformObserver();

        boolean isAdded = platform.addObserver(observer);
        assertTrue(isAdded);
        assertThat(platform.getObservers()).containsExactly(observer);

        boolean failedToAdd = platform.addObserver(observer);
        assertFalse(failedToAdd);
        assertThat(platform.getObservers()).containsExactly(observer);

        boolean isRemoved = platform.removeObserver(observer);
        assertTrue(isRemoved);
        assertThat(platform.getObservers()).isEmpty();

        boolean failedToRemove = platform.removeObserver(observer);
        assertFalse(failedToRemove);
    }

    @Test
    public void testAddContentEvent() {
        var observer = new MockPlatformObserver();
        Content mockContent = new MockContent("Mock Content");

        platform.addObserver(observer);
        platform.addContent(mockContent);

        AddContentEvent event = (AddContentEvent) observer.getEvent();
        assertThat(event.getAddedContent()).isSameAs(mockContent);
    }

    @Test
    public void testRemoveContentEvent() {
        var observer = new MockPlatformObserver();
        Content mockContent = new MockContent("Mock Content");
        platform.getContents().add(mockContent);

        platform.addObserver(observer);
        platform.removeContent(mockContent);

        RemoveContentEvent event = (RemoveContentEvent) observer.getEvent();
        assertThat(event.getRemovedContent()).isSameAs(mockContent);
    }

    @Test
    public void testUpdateContentEvent() {
        var observer = new MockPlatformObserver();
        Content oldContent = new MockContent.MockContentBuilder("Mock Content")
                .withDescription("Old description")
                .build();
        platform.getContents().add(oldContent);

        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("Updated description")
                .build();

        platform.addObserver(observer);
        platform.updateContent(updatedContent);

        UpdateContentEvent event = (UpdateContentEvent) observer.getEvent();
        assertThat(event.getOldContent()).isSameAs(oldContent);
        assertThat(event.getUpdatedContent()).isSameAs(updatedContent);
    }

    @Test
    public void testGetContentByTitleRunsCorrectly() {
        Content mockContent = new MockContent("Mock Content");
        platform.getContents().add(mockContent);

        assertThat(platform.getContentByTitle(mockContent.getTitle()))
                .contains(mockContent);

        assertThat(platform.getContentByTitle("Not existing content"))
                .isEmpty();
    }

    @Test
    public void testGetUserByUsernameRunsCorrectly() {
        platform.getUsers().add(user);

        assertThat(platform.getUserByUsername(user.getUsername()))
                .contains(user);

        assertThat(platform.getUserByUsername("not existing user"))
                .isEmpty();
    }
}