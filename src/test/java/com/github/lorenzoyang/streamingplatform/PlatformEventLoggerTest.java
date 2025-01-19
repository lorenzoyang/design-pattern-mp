package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.mocks.MockContent;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class PlatformEventLoggerTest {
    private StreamingPlatform platform;
    private PlatformEventLogger platformEventLogger;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", Stream::empty);
        this.platformEventLogger = new PlatformEventLogger();
        this.platform.addObserver(platformEventLogger);
    }

    @Test
    public void testNotifyChangeForAddContentEvent() {
        Content content = new MockContent("Mock Content");
        platform.addContent(content);

        String expected = "Content Added: {Title='Mock Content', Release Date='Release date not specified', free}";
        assertEquals(expected, platformEventLogger.getLogMessagesIterator().next());
    }

    @Test
    public void testNotifyChangeForRemoveContentEvent() {
        Content content = new MockContent("Mock Content");
        platform.getContents().add(content);
        platform.removeContent(content);

        String expected = "Content Removed: {Title='Mock Content', Release Date='Release date not specified', free}";
        assertEquals(expected, platformEventLogger.getLogMessagesIterator().next());
    }

    @Test
    public void testNotifyChangeForUpdateContentEvent() {
        Content oldContent = new MockContent.MockContentBuilder("Old Content")
                .withReleaseDate(LocalDate.of(2024, 1, 1))
                .build();
        platform.getContents().add(oldContent);
        Content updatedContent = new MockContent.MockContentBuilder(oldContent.getTitle())
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .build();
        platform.updateContent(updatedContent);

        String expected = "Content Updated: {Title='Old Content', Release Date='01-01-2025', free}";
        assertEquals(expected, platformEventLogger.getLogMessagesIterator().next());
    }

    @Test
    public void testClearLogs() {
        Content content = new MockContent("Mock Content");
        platform.addContent(content);
        platformEventLogger.clearLogs();

        assertThat(platformEventLogger.getLogMessagesIterator())
                .toIterable()
                .isEmpty();
    }
}