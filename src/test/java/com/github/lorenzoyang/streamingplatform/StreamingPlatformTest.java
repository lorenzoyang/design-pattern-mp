package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.mocks.MockContent;
import com.github.lorenzoyang.streamingplatform.mocks.MockPlatformObserver;
import com.github.lorenzoyang.streamingplatform.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
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
        Content content = new MockContent("Mock Content");
        int initialSize = platform.getContents().size();
        boolean isAdded = platform.addContent(content);

        assertTrue(isAdded);
        assertThat(platform.getContents())
                .hasSize(initialSize + 1)
                .contains(content);

        boolean failedToAdd = platform.addContent(content);
        assertFalse(failedToAdd);
        assertThat(platform.getContents()).hasSize(initialSize + 1);

        boolean isRemoved = platform.removeContent(content);
        assertTrue(isRemoved);
        assertThat(platform.getContents())
                .hasSize(initialSize)
                .doesNotContain(content);

        boolean failedToRemove = platform.removeContent(content);
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

        Content notExistingContent = new MockContent("Not existing content");
        boolean failedToUpdate = platform.updateContent(notExistingContent);
        assertFalse(failedToUpdate);
    }

    @Test
    public void testAddContentEvent() {
        var observer = new MockPlatformObserver();
        Content content = new MockContent("Mock Content");

        platform.addObserver(observer);
        platform.addContent(content);

        AddContentEvent event = (AddContentEvent) observer.getEvent();
        assertThat(event.getAddedContent()).isSameAs(content);
    }

    @Test
    public void testRemoveContentEvent() {
        var observer = new MockPlatformObserver();
        Content content = new MockContent("Mock Content");
        platform.getContents().add(content);

        platform.addObserver(observer);
        platform.removeContent(content);

        RemoveContentEvent event = (RemoveContentEvent) observer.getEvent();
        assertThat(event.getRemovedContent()).isSameAs(content);
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
    public void testWatchContentRunsCorrectly() {
        Content content = new MockContent("Mock Content") {
            @Override
            public int getDurationInMinutes() {
                return 10;
            }

            @Override
            public boolean isPremium() {
                return false;
            }
        };
        User subscribedUser = new User.UserBuilder("subscribedUser", "password")
                .subscribe()
                .build();
        platform.getContents().add(content);
        platform.getUsers().add(subscribedUser);

        int timeToWatch = 5;
        int expected = Math.min(timeToWatch, content.getDurationInMinutes());
        assertEquals(expected, platform.watchContent(subscribedUser, content, timeToWatch));

        timeToWatch = 15;
        expected = Math.min(timeToWatch, content.getDurationInMinutes());
        assertEquals(expected, platform.watchContent(subscribedUser, content, timeToWatch));
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNonExistingUser() {
        Content content = new MockContent("Mock Content");
        platform.getContents().add(content);

        assertThatThrownBy(() -> platform.watchContent(user, content, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User 'user' is not registered");
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content content = new MockContent("Mock Content");
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, content, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content 'Mock Content' does not exist");
    }

    @Test
    public void testWatchContentThrowsAccessDeniedExceptionForNonSubscribedUser() {
        Content content = new MockContent("Mock Content") {
            @Override
            public boolean isPremium() {
                return true;
            }
        };
        platform.getContents().add(content);
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, content, 10))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage("User 'user' does not have a subscription");
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        Content content = new MockContent("Mock Content");
        platform.getContents().add(content);
        platform.getUsers().add(user);

        assertThatThrownBy(() -> platform.watchContent(user, content, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch must be greater than 0");
        assertThatThrownBy(() -> platform.watchContent(user, content, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch must be greater than 0");
    }

    @Test
    public void testDisplayContentRunsCorrectly() {
        Content movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
                .requiresSubscription()
                .withDescription("Movie description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .build();
        platform.getContents().add(movie);

        String expected = "Movie:\n" +
                "  Title: Movie\n" +
                "  Description: Movie description\n" +
                "  Release date: 01-01-2025\n" +
                "  Duration: 120 minutes\n" +
                "  Requires subscription: No";
        assertEquals(expected, platform.displayContent(movie));

        Content tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .addEpisodes(1, List.of(
                        new Episode(1, 30),
                        new Episode(2, 30),
                        new Episode(3, 30)
                ))
                .withDescription("TVSeries description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .build();
        platform.getContents().add(tvSeries);
        expected = "TV Series:\n" +
                "  Title: TVSeries\n" +
                "  Description: TVSeries description\n" +
                "  Release date: 01-01-2025\n" +
                "  Duration: 90 minutes\n" +
                "  Requires subscription: Yes\n" +
                "  Season 1:\n" +
                "    Episodes: 1 2 3 ";
        assertEquals(expected, platform.displayContent(tvSeries));
    }

    @Test
    public void testDisplayContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content content = new MockContent("Mock Content");

        assertThatThrownBy(() -> platform.displayContent(content))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content 'Mock Content' does not exist");
    }


    @Test
    public void testGetContentByTitleRunsCorrectly() {
        Content content = new MockContent("Mock Content");
        platform.getContents().add(content);

        assertThat(platform.getContentByTitle(content.getTitle()))
                .contains(content);

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

}