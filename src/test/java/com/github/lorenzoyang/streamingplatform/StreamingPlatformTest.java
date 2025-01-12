package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.ReplaceContentEvent;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class StreamingPlatformTest {
    private DataProvider<User> userDataProvider;
    private DataProvider<Content> contentDataProvider;
    private StreamingPlatform streamingPlatform;

    private User user;

    @Before
    public void setUp() {
        this.userDataProvider = new MockUserDataProvider();
        this.contentDataProvider = new MockContentDataProvider();
        this.streamingPlatform = new StreamingPlatform("Streaming Platform", contentDataProvider, userDataProvider);
        // TODO add check for spaces in username
        this.user = new User.UserBuilder("StreamingPlatformTest_user", "password").build();
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new StreamingPlatform(null, contentDataProvider, userDataProvider))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Name cannot be null");

        assertThatThrownBy(() -> new StreamingPlatform("Streaming Platform", null, userDataProvider))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content provider cannot be null");

        assertThatThrownBy(() -> new StreamingPlatform("Streaming Platform", contentDataProvider, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User provider cannot be null");
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
        User existingUser = userDataProvider.fetchData().get(0);
        assertThatThrownBy(() -> streamingPlatform.registerUser(existingUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User already registered");
    }

    @Test
    public void testUnregisterUserThrowsIllegalArgumentExceptionForNonExistingUser() {
        assertThatThrownBy(() -> streamingPlatform.unregisterUser(user))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");

        assertThatThrownBy(() -> streamingPlatform.unregisterUser(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User does not exist");
    }

    @Test
    public void testGetContentByTitleRunsCorrectly() {
        Content existingContent = contentDataProvider.fetchData().get(0);
        assertThat(streamingPlatform.getContentByTitle(existingContent.getTitle()))
                .isPresent()
                .contains(existingContent);

        assertThat(streamingPlatform.getContentByTitle("Non existing title"))
                .isEmpty();
    }

    @Test
    public void testGetUserByUsernameRunsCorrectly() {
        User existingUser = userDataProvider.fetchData().get(0);
        assertThat(streamingPlatform.getUserByUsername(existingUser.getUsername()))
                .isPresent()
                .contains(existingUser);

        assertThat(streamingPlatform.getUserByUsername("Non existing username"))
                .isEmpty();
    }


    @Test
    public void testAddContentThrowsNullPointerExceptionForNullContent() {
        assertThatThrownBy(() -> streamingPlatform.addContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testAddContentThrowsIllegalArgumentExceptionForExistingContent() {
        Content existingContent = contentDataProvider.fetchData().get(0);
        assertThatThrownBy(() -> streamingPlatform.addContent(existingContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content already exists");
    }

    @Test
    public void testAddContentRunsCorrectly() {
        Content newContent = new TVSeries.TVSeriesBuilder("StreamingPlatformTest TV Series").build();
        streamingPlatform.addContent(newContent);

        assertThat(streamingPlatform.getContents()).contains(newContent);
        assertThat(streamingPlatform.getContents())
                .hasSize(contentDataProvider.fetchData().size() + 1);
    }

    @Test
    public void testRemoveContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content newContent = new TVSeries.TVSeriesBuilder("StreamingPlatformTest TV Series").build();
        assertThatThrownBy(() -> streamingPlatform.removeContent(newContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content does not exist");
    }

    @Test
    public void testRemoveContentRunsCorrectly() {
        Content existingContent = contentDataProvider.fetchData().get(0);
        streamingPlatform.removeContent(existingContent);

        assertThat(streamingPlatform.getContents()).doesNotContain(existingContent);
        assertThat(streamingPlatform.getContents())
                .hasSize(contentDataProvider.fetchData().size() - 1);
    }

    @Test
    public void testReplaceContentThrowsIllegalArgumentExceptionForNonExistingContent() {
        Content newContent = new TVSeries.TVSeriesBuilder("StreamingPlatformTest TV Series").build();
        assertThatThrownBy(() -> streamingPlatform.replaceContent(newContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The content to be replaced does not exist in the platform.");
    }

    @Test
    public void testReplaceContentRunsCorrectly() {
        Content oldContent = contentDataProvider.fetchData().get(0);
        var releaseDate = LocalDate.now();
        Content newContent = new TVSeries.TVSeriesBuilder(oldContent.getTitle())
                .withDescription("New description")
                .withReleaseDate(releaseDate)
                .build();
        streamingPlatform.replaceContent(newContent);

        Content content = streamingPlatform.getContents().stream()
                .filter(c -> c.equals(oldContent))
                .findFirst()
                .orElse(null);
        assertThat(content).isNotNull();
        assertThat(content.getDescription()).isEqualTo("New description");
        assertThat(content.getReleaseDate()).isEqualTo(releaseDate);
    }

    @Test
    public void testAddContentEvent() {
        var mockObserver = new MockObserver();
        streamingPlatform.attach(mockObserver);

        Content newContent = new TVSeries.TVSeriesBuilder("StreamingPlatformTest TV Series").build();
        streamingPlatform.addContent(newContent);

        AddContentEvent event = (AddContentEvent) mockObserver.getEvent();
        assertThat(event.getAddedContent()).isSameAs(newContent);
    }

    @Test
    public void testRemoveContentEvent() {
        var mockObserver = new MockObserver();
        streamingPlatform.attach(mockObserver);

        Content existingContent = contentDataProvider.fetchData().get(0);
        streamingPlatform.removeContent(existingContent);

        RemoveContentEvent event = (RemoveContentEvent) mockObserver.getEvent();
        assertThat(event.getRemovedContent()).isSameAs(existingContent);
    }

    @Test
    public void testReplaceContentEvent() {
        var mockObserver = new MockObserver();
        streamingPlatform.attach(mockObserver);

        Content oldContent = contentDataProvider.fetchData().get(0);
        Content newContent = new TVSeries.TVSeriesBuilder(oldContent.getTitle())
                .withDescription("New description")
                .build();
        streamingPlatform.replaceContent(newContent);

        ReplaceContentEvent event = (ReplaceContentEvent) mockObserver.getEvent();
        assertThat(event.getOldContent()).isSameAs(oldContent);
        assertThat(event.getNewContent()).isSameAs(newContent);
    }
}