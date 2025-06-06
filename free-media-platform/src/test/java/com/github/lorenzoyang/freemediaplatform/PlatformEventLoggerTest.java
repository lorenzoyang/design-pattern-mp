package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.content.Episode;
import com.github.lorenzoyang.freemediaplatform.content.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PlatformEventLoggerTest {
    private StreamingPlatform platform;
    private PlatformEventLogger platformEventLogger;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform",
                () -> List.of(new Movie.MovieBuilder("Movie", new Episode(1, 120))
                        .build())
        );
        this.platformEventLogger = new PlatformEventLogger();
        this.platform.addObserver(platformEventLogger);
    }

    @Test
    public void testNotifyChangeForAddContentEvent() {
        Content contentToAdd = new Movie.MovieBuilder("contentToAdd", new Episode(1, 120))
                .build();
        platform.addContent(contentToAdd);
        String expected = "Content Added: {" +
                "Title='contentToAdd', " +
                "Description='No description available', " +
                "Release Date='Release date not specified', " +
                "Resolution='Resolution not specified'" +
                "}";
        assertEquals(expected, platformEventLogger.logMessagesIterator().next());
    }

    @Test
    public void testNotifyChangeForRemoveContentEvent() {
        Content contentToRemove = platform.contentIterator().next();
        platform.removeContent(contentToRemove);
        String expected = "Content Removed: {" +
                "Title='Movie', " +
                "Description='No description available', " +
                "Release Date='Release date not specified', " +
                "Resolution='Resolution not specified'" +
                "}";
        assertEquals(expected, platformEventLogger.logMessagesIterator().next());
    }

    @Test
    public void testNotifyChangeForUpdateContentEvent() {
        Content contentToUpdate = platform.contentIterator().next();
        Content updatedContent = new Movie.MovieBuilder(contentToUpdate.getTitle(),
                new Episode(1, 1))
                .build();
        platform.updateContent(updatedContent);

        String expected = "Content Updated: \n" +
                "\tfrom: {Title='Movie', Description='No description available', " +
                "Release Date='Release date not specified', " +
                "Resolution='Resolution not specified'}\n" +
                "\tto: {Title='Movie', Description='No description available', " +
                "Release Date='Release date not specified', " +
                "Resolution='Resolution not specified'}";
        assertEquals(expected, platformEventLogger.logMessagesIterator().next());
    }

    @Test
    public void testClearLogMessagesRunsCorrectly() {
        this.platformEventLogger.getLogMessages().add("Test log message");
        this.platformEventLogger.clearLogs();
        assertEquals(0, this.platformEventLogger.getLogMessages().size());
    }
}