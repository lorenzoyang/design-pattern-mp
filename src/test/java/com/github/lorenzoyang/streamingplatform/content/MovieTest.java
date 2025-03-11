package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MovieTest {
    private Episode episode;

    @Before
    public void setUp() {
        this.episode = new Episode(1, 120);
    }

    @Test
    public void testMovieBuilderCreatesMovieWithValidArguments() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        Movie movie = new Movie.MovieBuilder("Movie", episode)
                .withDescription("Movie description")
                .withReleaseDate(releaseDate)
                .build();

        assertEquals("Movie", movie.getTitle());
        assertEquals(episode, movie.getEpisode());
        assertThat(movie.getDescription()).contains("Movie description");
        assertThat(movie.getReleaseDate()).contains(releaseDate);
        assertEquals(120, movie.getDurationInMinutes());
    }

    @Test
    public void testMovieBuilderConstructorThrowsNullPointerExceptionForNullTitle() {
        assertThatThrownBy(() -> new Movie.MovieBuilder(null, episode))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content title cannot be null");
    }

    @Test
    public void testMovieBuilderConstructorThrowsInvalidContentExceptionForBlankTitle() {
        assertThatThrownBy(() -> new Movie.MovieBuilder("    ", episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
        assertThatThrownBy(() -> new Movie.MovieBuilder("", episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
    }

    @Test
    public void testMovieBuilderConstructorThrowsNullPointerExceptionForNullEpisode() {
        assertThatThrownBy(() -> new Movie.MovieBuilder("Movie", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testMovieBuilderConstructorThrowsInvalidContentExceptionForInvalidEpisode() {
        var episode = new Episode(2, 120);

        assertThatThrownBy(() -> new Movie.MovieBuilder("Movie", episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("A movie can only have one episode with episode number 1");
    }

    @Test
    public void testWithDescriptionThrowsNullPointerExceptionForNullDescription() {
        var builder = new Movie.MovieBuilder("Movie", episode);

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content description cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForBlankDescription() {
        var builder = new Movie.MovieBuilder("Movie", episode);

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
        assertThatThrownBy(() -> builder.withDescription(""))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
    }

    @Test
    public void testWithReleaseDateThrowsNullPointerExceptionForNullReleaseDate() {
        var builder = new Movie.MovieBuilder("Movie", episode);

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content release date cannot be null");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("Movie", episode).build();

        assertEquals(episode.getDurationInMinutes(), movie.getDurationInMinutes());
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        Movie movie1 = new Movie.MovieBuilder("Movie", episode).build();
        Movie movie2 = new Movie.MovieBuilder("Movie", episode).build();

        assertEquals(movie1, movie2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        Movie movie1 = new Movie.MovieBuilder("Movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("Movie2", episode).build();

        assertNotEquals(movie1, movie2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        Movie movie1 = new Movie.MovieBuilder("Movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("Movie1", episode).build();
        Movie movie3 = new Movie.MovieBuilder("Movie2", episode).build();

        assertEquals(movie1.hashCode(), movie2.hashCode());
        assertNotEquals(movie1.hashCode(), movie3.hashCode());
        assertNotEquals(movie2.hashCode(), movie3.hashCode());
    }
}