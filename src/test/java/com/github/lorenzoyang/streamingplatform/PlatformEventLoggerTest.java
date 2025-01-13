package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.MockContent;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class PlatformEventLoggerTest {
    private StreamingPlatform platform;
    private PlatformEventLogger logger;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", new MockContentProvider());
        this.logger = new PlatformEventLogger();
        this.platform.addObserver(logger);
    }

    @Test
    public void testUpdateForAddContentEvent() {
        Content mockContent1 = new MockContent("Mock Content 1");
        platform.addContent(mockContent1);

        String expectedLogMessage =
                "Content Added: {name='Mock Content 1', description='Description not provided', free}";
        assertThat(logger.getLogMessages()).hasSize(1);
        assertThat(logger.getLogMessages().get(0)).isEqualTo(expectedLogMessage);

        Content mockContent2 = new MockContent("Mock Content 2");
        platform.addContent(mockContent2);

        expectedLogMessage =
                "Content Added: {name='Mock Content 2', description='Description not provided', free}";
        assertThat(logger.getLogMessages()).hasSize(2);
        assertThat(logger.getLogMessages().get(1)).isEqualTo(expectedLogMessage);
    }

    @Test
    public void testUpdateForRemoveContentEvent() {
        Content existingContent1 = platform.getContents().get(0);
        platform.removeContent(existingContent1);

        String expectedLogMessage =
                "Content Removed: {name='movie1', description='Description of movie1', free}";
        assertThat(logger.getLogMessages()).hasSize(1);
        assertThat(logger.getLogMessages().get(0)).isEqualTo(expectedLogMessage);

        Content existingContent2 = platform.getContents().get(1);
        platform.removeContent(existingContent2);

        expectedLogMessage =
                "Content Removed: {name='tvSeries1', description='Description of tvSeries1', premium}";
        assertThat(logger.getLogMessages()).hasSize(2);
        assertThat(logger.getLogMessages().get(1)).isEqualTo(expectedLogMessage);
    }

    @Test
    public void testUpdateForUpdateContentEvent() {
        Content mockOldContent = new MockContent("Old Content Name");
        platform.getContents().add(mockOldContent);

        Content mockUpdatedContent = new MockContent.MockContentBuilder(mockOldContent.getTitle())
                .withDescription("New Description")
                .build();
        platform.updateContent(mockUpdatedContent);

        String expectedLogMessage =
                "Content Updated: {name='Old Content Name', description='New Description', free}";
        assertThat(logger.getLogMessages()).hasSize(1);
        assertThat(logger.getLogMessages().get(0)).isEqualTo(expectedLogMessage);
    }

    @Test
    public void testClearLogsRunsCorrectly() {
        Content mockContent = new MockContent("Mock Content");
        platform.addContent(mockContent);

        String expectedLogMessage =
                "Content Added: {name='Mock Content', description='Description not provided', free}";
        assertThat(logger.getLogMessages()).hasSize(1);
        assertThat(logger.getLogMessages().get(0)).isEqualTo(expectedLogMessage);

        logger.clearLogs();
        assertThat(logger.getLogMessages()).isEmpty();
    }
}