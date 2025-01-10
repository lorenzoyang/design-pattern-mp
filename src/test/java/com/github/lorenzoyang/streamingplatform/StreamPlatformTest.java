package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StreamPlatformTest {
    private DataProvider<User> userDataProvider;
    private DataProvider<Content> contentDataProvider;

    @Before
    public void setUp() {
        this.userDataProvider = new MockUserDataProvider();
        this.contentDataProvider = new MockContentDataProvider();
    }

    @Test
    public void testAttachAndDetachRunCorrectly() {
        var streamingPlatform = new StreamPlatform("Streaming Platform", contentDataProvider, userDataProvider);
        PlatformObserver observer = (event) -> {
        }; // Empty observer for testing purposes

        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size());
        streamingPlatform.attach(observer);
        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size() + 1);
        streamingPlatform.detach(observer);
        assertThat(streamingPlatform.getObservers())
                .hasSize(userDataProvider.fetchData().size());
    }
}