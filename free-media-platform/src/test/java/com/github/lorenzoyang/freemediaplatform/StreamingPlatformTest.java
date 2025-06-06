package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.*;
import com.github.lorenzoyang.freemediaplatform.exceptions.StreamingPlatformException;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class StreamingPlatformTest {
    private StreamingPlatform platform;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform", () -> List.of(
                new Movie.MovieBuilder("Movie1", new Episode(1, 1)).build(),
                new TVSeries.TVSeriesBuilder("TVSeries1", new Season(1, List.of(
                        new Episode(1, 1),
                        new Episode(2, 1)
                ))).build()
        ));
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new StreamingPlatform(null, List::of))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Streaming platform name cannot be null");

        assertThatThrownBy(() -> new StreamingPlatform("Streaming Platform", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content provider cannot be null");
    }

    @Test
    public void testConstructorThrowsStreamingPlatformExceptionForBlankName() {
        assertThatThrownBy(() -> new StreamingPlatform(" ", List::of))
                .isInstanceOf(StreamingPlatformException.class)
                .hasMessage("Streaming platform name cannot be blank");
    }

    @Test
    public void testGetContentByTitleRunsCorrectly() {
        Optional<Content> content = this.platform.getContentByTitle("Movie1");
        assertThat(content).isPresent();
        assertEquals("Movie1", content.get().getTitle());
        Optional<Content> nonExistentContent = this.platform.getContentByTitle("NonExistent");
        assertThat(nonExistentContent).isEmpty();
    }

    @Test
    public void testAddContentAndRemoveContentCorrectly() {
        Content newMovie = new Movie.MovieBuilder("NewMovie", new Episode(1, 1))
                .build();

        boolean added = this.platform.addContent(newMovie);
        assertTrue(added);
        assertThat(this.platform.contentIterator())
                .toIterable()
                .contains(newMovie);
        added = this.platform.addContent(newMovie);
        assertFalse(added);

        boolean removed = this.platform.removeContent(newMovie);
        assertTrue(removed);
        assertThat(this.platform.contentIterator())
                .toIterable()
                .doesNotContain(newMovie);
        removed = this.platform.removeContent(newMovie);
        assertFalse(removed);
    }

    @Test
    public void testUpdateContentCorrectly() {
        Content oldContent = this.platform.contentIterator().next();
        int initialContentCount = this.platform.getContents().size();
        Content updatedContent = new Movie.MovieBuilder(oldContent.getTitle(),
                new Episode(1, 1))
                .withDescription("Updated description")
                .build();

        boolean updated = this.platform.updateContent(updatedContent);
        assertTrue(updated);
        assertEquals(initialContentCount, this.platform.getContents().size());
        assertThat(this.platform.contentIterator())
                .toIterable()
                .contains(updatedContent);
    }

    @Test
    public void testDisplayContentRunsCorrectly() {
        Content content = this.platform.contentIterator().next();
        String displayedContent = this.platform.displayContent(content);

        String expected = "From 'Streaming Platform' platform:\n" +
                "Movie: Movie1\n" +
                "  Description: No description available\n" +
                "  Release Date: Release date not specified\n" +
                "  Resolution: Resolution not specified\n" +
                "  Total Duration: 1 minutes\n";
        assertEquals(expected, displayedContent);
    }

    @Test
    public void testDisplayContentThrowsNullPointerExceptionForNullContent() {
        assertThatThrownBy(() -> this.platform.displayContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testDisplayContentThrowsStreamingPlatformExceptionForNonExistentContent() {
        Content nonExistentContent = new Movie.MovieBuilder("NonExistent",
                new Episode(1, 1))
                .build();
        assertThatThrownBy(() -> this.platform.displayContent(nonExistentContent))
                .isInstanceOf(StreamingPlatformException.class)
                .hasMessage("Content 'NonExistent' does not exist");
    }

    @Test
    public void testWatchContentRunsCorrectly() {
        Iterator<Content> contentIterator = this.platform.contentIterator();

        Content movie = contentIterator.next();
        Iterable<Episode> episodes = this.platform.watchContent(movie);
        assertEquals(List.of(new Episode(1, 1)), episodes);

        Content tvSeries = contentIterator.next();
        episodes = this.platform.watchContent(tvSeries);
        assertEquals(List.of(new Episode(1, 1), new Episode(2, 1)), episodes);
    }

    @Test
    public void testWatchContentThrowsNullPointerExceptionForNullContent() {
        assertThatThrownBy(() -> this.platform.watchContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testWatchContentThrowsStreamingPlatformExceptionForNonExistentContent() {
        Content nonExistentContent = new Movie.MovieBuilder("NonExistent",
                new Episode(1, 1))
                .build();
        assertThatThrownBy(() -> this.platform.watchContent(nonExistentContent))
                .isInstanceOf(StreamingPlatformException.class)
                .hasMessage("Content 'NonExistent' does not exist");
    }

    @Test
    public void testAddObserverAndRemoveObserver() {
        // mock observer for testing
        PlatformObserver observer = e -> {
        };

        platform.addObserver(observer);
        assertEquals(1, platform.getObservers().size());
        assertThat(platform.getObservers()).containsExactly(observer);

        platform.removeObserver(observer);
        assertThat(platform.getObservers()).doesNotContain(observer);
        assertEquals(0, platform.getObservers().size());
    }
}