package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.mocks.MockContent;
import com.github.lorenzoyang.streamingplatform.user.User;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class UserWatchlistManagerTest {
    private StreamingPlatform platform;
    private UserWatchlistManager userWatchlistManager;
    private User user;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", Stream::empty);
        this.userWatchlistManager = new UserWatchlistManager();
        this.user = new User.UserBuilder("username", "password").build();

        this.platform.addObserver(userWatchlistManager);
        this.platform.registerUser(user);
    }

    @Test
    public void testNotifyChangeForRemoveContentEventForInProgressContents() {
        Content content = new MockContent("Mock Content");
        platform.addContent(content);

        userWatchlistManager.getInProgressContents().put(user, new HashSet<>(Set.of(content)));

        platform.removeContent(content);

        assertThat(userWatchlistManager.getInProgressContentsIterator(user))
                .toIterable()
                .isEmpty();
    }

    @Test
    public void testNotifyChangeForRemoveContentEventForCompletedContents() {
        Content content = new MockContent("Mock Content");
        platform.addContent(content);

        userWatchlistManager.getCompletedContents().put(user, new HashSet<>(Set.of(content)));

        platform.removeContent(content);

        assertThat(userWatchlistManager.getCompletedContentsIterator(user))
                .toIterable()
                .isEmpty();
    }

    @Test
    public void testNotifyChangeForUpdateContentEventForInProgressContents() {
        Content oldContent = new MockContent.MockContentBuilder("Old Content")
                .withDescription("Old Description")
                .build();
        platform.addContent(oldContent);

        userWatchlistManager.getInProgressContents().put(user, new HashSet<>(Set.of(oldContent)));

        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("Updated Description")
                .build();
        platform.updateContent(updatedContent);

        assertThat(userWatchlistManager.getInProgressContentsIterator(user))
                .toIterable()
                .containsExactly(updatedContent);
        assertThat(userWatchlistManager.getInProgressContentsIterator(user).next())
                .isEqualTo(updatedContent);
    }

    @Test
    public void testNotifyChangeForUpdateContentEventForCompletedContents() {
        Content oldContent = new MockContent.MockContentBuilder("Old Content")
                .withDescription("Old Description")
                .build();
        platform.addContent(oldContent);

        userWatchlistManager.getCompletedContents().put(user, new HashSet<>(Set.of(oldContent)));

        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withDescription("Updated Description")
                .build();
        platform.updateContent(updatedContent);

        assertThat(userWatchlistManager.getCompletedContentsIterator(user))
                .toIterable()
                .containsExactly(updatedContent);
        assertThat(userWatchlistManager.getCompletedContentsIterator(user).next())
                .isEqualTo(updatedContent);
    }

    @Test
    public void testNotifyChangeForWatchContentEvent() {
        Content content = new MockContent("Mock Content") {
            @Override
            public int getDurationInMinutes() {
                return 10;
            }
        };
        platform.addContent(content);

        platform.watchContent(user, content, 5);
        assertThat(userWatchlistManager.getInProgressContentsIterator(user))
                .toIterable()
                .containsExactly(content);

        platform.watchContent(user, content, 10);
        assertThat(userWatchlistManager.getInProgressContentsIterator(user))
                .toIterable()
                .isEmpty();
        assertThat(userWatchlistManager.getCompletedContentsIterator(user))
                .toIterable()
                .containsExactly(content);
    }
}