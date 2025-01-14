package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.MockContent;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.utils.DownloadResult;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StreamingPlatformTest {
    private StreamingPlatform platform;

    private User user;

    private User registeredUser;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", new MockContentProvider());

        this.user = new User.UserBuilder("username", "password").build();

        this.registeredUser = new User.UserBuilder("registeredUsername", "password")
                .subscribe()
                .build();
        this.platform.getUsers().add(registeredUser);
        this.platform.getObservers().add(registeredUser);
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new StreamingPlatform(null, List::of))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Name cannot be null");

        assertThatThrownBy(() -> new StreamingPlatform("Streaming Platform", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content provider cannot be null");
    }

    @Test
    public void testAddObserverAndRemoveObserverRunCorrectly() {
        PlatformObserver observer = (event) -> {
            // Just for testing
        };

        platform.addObserver(observer);
        assertThat(platform.getObservers()).hasSize(2);
        assertThat(platform.getObservers()).contains(observer);

        platform.removeObserver(observer);
        assertThat(platform.getObservers()).hasSize(1);
        assertThat(platform.getObservers()).doesNotContain(observer);
    }

    @Test
    public void testRegisterUserAndUnregisterUserRunCorrectly() {
        platform.registerUser(user);
        assertThat(platform.getUsers()).hasSize(2);
        assertThat(platform.getUsers()).contains(user);

        platform.unregisterUser(user);
        assertThat(platform.getUsers()).hasSize(1);
        assertThat(platform.getUsers()).doesNotContain(user);
    }

    @Test
    public void testRegisterUserThrowsNullPointerExceptionForNullUser() {
        assertThatThrownBy(() -> platform.registerUser(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");
    }

    @Test
    public void testRegisterUserThrowsIllegalArgumentExceptionForExistingUser() {
        assertThatThrownBy(() -> platform.registerUser(registeredUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already registered");
    }

    @Test
    public void testUnregisterUserThrowsIllegalArgumentExceptionForNonExistingUser() {
        assertThatThrownBy(() -> platform.unregisterUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");

        assertThatThrownBy(() -> platform.unregisterUser(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");
    }

    @Test
    public void testGetContentByTitleRunsCorrectly() {
        Content existingContent = platform.getContents().get(0);
        assertThat(platform.getContentByTitle(existingContent.getTitle()))
                .isPresent()
                .contains(existingContent);

        assertThat(platform.getContentByTitle("Non existing title"))
                .isEmpty();
    }

    @Test
    public void testGetUserByUsernameRunsCorrectly() {
        assertThat(platform.getUserByUsername(registeredUser.getUsername()))
                .isPresent()
                .contains(registeredUser);

        assertThat(platform.getUserByUsername("Non existing username"))
                .isEmpty();
    }

    @Test
    public void testAddContentAndRemoveContentRunCorrectly() {
        Content mockContent = new MockContent("Mock Content");
        int initialSize = platform.getContents().size();

        platform.addContent(mockContent);
        assertThat(platform.getContents()).hasSize(initialSize + 1);
        assertThat(platform.getContents()).contains(mockContent);

        platform.removeContent(mockContent);
        assertThat(platform.getContents()).hasSize(initialSize);
        assertThat(platform.getContents()).doesNotContain(mockContent);
    }

    @Test
    public void testAddContentThrowsNullPointerExceptionForNullContent() {
        assertThatThrownBy(() -> platform.addContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testAddContentThrowsIllegalArgumentExceptionForExistingContent() {
        Content existingContent = platform.getContents().get(0);
        assertThatThrownBy(() -> platform.addContent(existingContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content already exists");
    }

    @Test
    public void testRemoveContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content mockContent = new MockContent("Mock Content");

        assertThatThrownBy(() -> platform.removeContent(mockContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content does not exist");
    }

    @Test
    public void testUpdateContentRunsCorrectly() {
        Content oldContent = new MockContent("Old Content Name");
        platform.getContents().add(oldContent);
        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("New description")
                .build();

        platform.updateContent(updatedContent);

        Content content = platform.getContents().stream()
                .filter(c -> c.equals(oldContent))
                .findFirst()
                .orElse(null);
        assertThat(content).isNotNull();
        assertThat(content.getDescription()).isEqualTo("New description");
    }

    @Test
    public void testUpdateContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content mockContent = new MockContent("Mock Content");
        assertThatThrownBy(() -> platform.updateContent(mockContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The content to be updated does not exist in the platform.");
    }

    @Test
    public void testAddContentEvent() {
        var mockObserver = new MockObserver();
        platform.addObserver(mockObserver);

        Content mockNewContent = new MockContent("New Content");
        platform.addContent(mockNewContent);

        AddContentEvent event = (AddContentEvent) mockObserver.getEvent();
        assertThat(event.getAddedContent()).isSameAs(mockNewContent);
    }

    @Test
    public void testRemoveContentEvent() {
        var mockObserver = new MockObserver();
        platform.addObserver(mockObserver);

        Content existingContent = platform.getContents().get(0);
        platform.removeContent(existingContent);

        RemoveContentEvent event = (RemoveContentEvent) mockObserver.getEvent();
        assertThat(event.getRemovedContent()).isSameAs(existingContent);
    }

    @Test
    public void testUpdateContentEvent() {
        var mockObserver = new MockObserver();
        platform.addObserver(mockObserver);

        Content oldContent = new MockContent("Old Content Name");
        platform.getContents().add(oldContent);
        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("New description")
                .build();
        platform.updateContent(updatedContent);

        UpdateContentEvent event = (UpdateContentEvent) mockObserver.getEvent();
        assertThat(event.getOldContent()).isSameAs(oldContent);
        assertThat(event.getUpdatedContent()).isSameAs(updatedContent);
    }

    @Test
    public void testDisplayContentRunsCorrectly() {
        Content existingContent = platform.getContents().get(0);

        String expectedMsg = "Title: movie1\n" +
                "Description: Description of movie1\n" +
                "Release Date: 01 Jan 2025\n" +
                "Total duration: 120 minutes\n" +
                "Episode 1";
        assertThat(platform.displayContent(existingContent)).isEqualTo(expectedMsg);
    }

    @Test
    public void testDisplayContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content mockContent = new MockContent("Mock Content");
        assertThatThrownBy(() -> platform.displayContent(mockContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content does not exist in the platform");
    }

    @Test
    public void testDownloadContentRunsCorrectly() {
        Content notFreeExistignContent = new TVSeries.TVSeriesBuilder("testDownloadContentRunsCorrectlyTvSeries")
                .requiresSubscription()
                .build();
        platform.getContents().add(notFreeExistignContent);

        DownloadResult result = platform.downloadContent(registeredUser, notFreeExistignContent);

        String expectedMsg = "Downloading TV series: testDownloadContentRunsCorrectlyTvSeries\n";
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo(expectedMsg);

        assertThat(platform.getDownloadHistory()).hasSize(1);
        assertThat(platform.getDownloadHistory().keySet()).containsExactly(registeredUser);
        assertThat(platform.getDownloadHistory().get(registeredUser)).containsExactly(result);

        User notSubscribed = this.user;
        platform.getUsers().add(notSubscribed);
        DownloadResult notSubscribedResult = platform.downloadContent(notSubscribed, notFreeExistignContent);

        expectedMsg = "You need a subscription to download this TV series";
        assertThat(notSubscribedResult.isSuccess()).isFalse();
        assertThat(notSubscribedResult.getMessage()).isEqualTo(expectedMsg);

        assertThat(platform.getDownloadHistory()).hasSize(2);
        assertThat(platform.getDownloadHistory().keySet()).containsExactly(registeredUser, notSubscribed);
        assertThat(platform.getDownloadHistory().get(notSubscribed)).containsExactly(notSubscribedResult);
    }

    @Test
    public void testDownloadContentThrowsIllegalArgumentExceptionForNonExistingUser() {
        Content existingContent = platform.getContents().get(0);
        assertThatThrownBy(() -> platform.downloadContent(user, existingContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not yet registered");
    }

    @Test
    public void testDownloadContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        assertThatThrownBy(() -> platform.downloadContent(registeredUser, new MockContent("Mock Content")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content does not exist in the platform");
    }

    @Test
    public void testGetDownloadHistoryRunsCorrectly() {
        Content existingContent = platform.getContents().get(0);
        DownloadResult result = platform.downloadContent(registeredUser, existingContent);

        assertThat(platform.getDownloadHistory(registeredUser))
                .toIterable()
                .containsExactly(result);
    }

    @Test
    public void testGetDownloadHistoryThrowsIllegalArgumentExceptionForNonExistingUser() {
        assertThatThrownBy(() -> platform.getDownloadHistory(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not yet registered");
    }
}