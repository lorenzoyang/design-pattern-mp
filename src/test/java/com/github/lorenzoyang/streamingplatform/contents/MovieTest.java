package com.github.lorenzoyang.streamingplatform.contents;

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
        Movie movie = new Movie.MovieBuilder("movie", episode)
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .build();

        assertEquals("movie", movie.getTitle());
        assertEquals(episode, movie.getEpisode());
        assertThat(movie.getDescription()).contains("description");
        assertThat(movie.getReleaseDate()).contains(releaseDate);
        assertEquals(120, movie.getDurationInMinutes());
    }

    @Test
    public void testMovieBuilderConstructorThrowsInvalidContentExceptionForInvalidTitle() {
        assertThatThrownBy(() -> new Movie.MovieBuilder(null, episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");

        assertThatThrownBy(() -> new Movie.MovieBuilder("    ", episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");
    }

    @Test
    public void testMovieBuilderConstructorThrowsNullPointerExceptionForNullEpisode() {
        assertThatThrownBy(() -> new Movie.MovieBuilder("movie", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testMovieBuilderConstructorThrowsInvalidContentExceptionForInvalidEpisode() {
        var episode = new Episode(2, 120);

        assertThatThrownBy(() -> new Movie.MovieBuilder("movie", episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Movie can only have one episode");
    }

    @Test
    public void testWithDescriptionThrowsNullPointerExceptionForNullDescription() {
        var builder = new Movie.MovieBuilder("movie", episode);

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Description cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForBlankDescription() {
        var builder = new Movie.MovieBuilder("movie", episode);

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be blank");
    }

    @Test
    public void testWithReleaseDateThrowsNullPointerExceptionForNullReleaseDate() {
        var builder = new Movie.MovieBuilder("movie", episode);

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Release date cannot be null");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("movie", episode).build();

        assertEquals(episode.getDurationInMinutes(), movie.getDurationInMinutes());
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie", episode).build();

        assertEquals(movie1, movie2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie2", episode).build();

        assertNotEquals(movie1, movie2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie3 = new Movie.MovieBuilder("movie2", episode).build();

        assertEquals(movie1.hashCode(), movie2.hashCode());
        assertNotEquals(movie1.hashCode(), movie3.hashCode());
        assertNotEquals(movie2.hashCode(), movie3.hashCode());
    }
}