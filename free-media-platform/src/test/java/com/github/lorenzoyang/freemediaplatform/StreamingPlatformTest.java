package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.*;
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
    public void testGetContentByTitleRunsCorrectly() {
        Optional<Content> content = platform.getContentByTitle("Movie1");
        assertThat(content).isPresent();
        assertEquals("Movie1", content.get().getTitle());
        Optional<Content> nonExistentContent = platform.getContentByTitle("NonExistent");
        assertThat(nonExistentContent).isEmpty();
    }

    @Test
    public void testAddContentAndRemoveContentCorrectly() {
        Content newMovie = new Movie.MovieBuilder("NewMovie", new Episode(1, 1))
                .build();

        boolean added = platform.addContent(newMovie);
        assertTrue(added);
        assertThat(platform.contentIterator())
                .toIterable()
                .contains(newMovie);
        added = platform.addContent(newMovie);
        assertFalse(added);

        boolean removed = platform.removeContent(newMovie);
        assertTrue(removed);
        assertThat(platform.contentIterator())
                .toIterable()
                .doesNotContain(newMovie);
        removed = platform.removeContent(newMovie);
        assertFalse(removed);
    }

    @Test
    public void testUpdateContentCorrectly() {
        Content contentToUpdate = platform.contentIterator().next();
        int initialContentCount = platform.getContents().size();
        Content updatedContent = new Movie.MovieBuilder(contentToUpdate.getTitle(),
                new Episode(1, 1))
                .withDescription("Updated description")
                .build();

        boolean updated = platform.updateContent(updatedContent);
        assertTrue(updated);
        assertEquals(initialContentCount, platform.getContents().size());
        assertThat(platform.contentIterator())
                .toIterable()
                .contains(updatedContent);
    }

    @Test
    public void testDisplayContentRunsCorrectly() {
        Content content = platform.contentIterator().next();
        String displayedContent = platform.displayContent(content);

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
        assertThatThrownBy(() -> platform.displayContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testDisplayContentThrowsIllegalArgumentExceptionForNonExistentContent() {
        Content nonExistentContent = new Movie.MovieBuilder("NonExistent",
                new Episode(1, 1))
                .build();
        assertThatThrownBy(() -> platform.displayContent(nonExistentContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content 'NonExistent' does not exist");
    }

    @Test
    public void testWatchContentRunsCorrectly() {
        Iterator<Content> contentIterator = platform.contentIterator();

        Content movie = contentIterator.next();
        Iterable<Episode> episodes = platform.watchContent(movie);
        assertEquals(List.of(new Episode(1, 1)), episodes);

        Content tvSeries = contentIterator.next();
        episodes = platform.watchContent(tvSeries);
        assertEquals(List.of(new Episode(1, 1), new Episode(2, 1)), episodes);
    }

    @Test
    public void testWatchContentThrowsNullPointerExceptionForNullContent() {
        assertThatThrownBy(() -> platform.watchContent(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content cannot be null");
    }

    @Test
    public void testWatchContentThrowsIllegalArgumentExceptionForNonExistentContent() {
        Content nonExistentContent = new Movie.MovieBuilder("NonExistent",
                new Episode(1, 1))
                .build();
        assertThatThrownBy(() -> platform.watchContent(nonExistentContent))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Content 'NonExistent' does not exist");
    }

    @Test
    public void testAddObserverAndRemoveObserver() {
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