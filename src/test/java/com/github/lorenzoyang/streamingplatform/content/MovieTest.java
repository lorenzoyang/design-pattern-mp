package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.User;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MovieTest {
    private Episode episode;
    private LocalDate releaseDate;

    @Before
    public void setUp() {
        this.episode = new Episode(1, 120);
        this.releaseDate = LocalDate.of(2025, 1, 1);
    }

    @Test
    public void testMovieBuilderCreatesMovieWithValidArguments() {
        Movie movie = new Movie.MovieBuilder("movie1", episode)
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .build();

        assertThat(movie.getTitle()).isEqualTo("movie1");
        assertThat(movie.getDescription()).isEqualTo("description");
        assertThat(movie.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(movie.getEpisode()).isEqualTo(episode);
        assertThat(movie.isFree()).isFalse();
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
        assertThatThrownBy(() -> new Movie.MovieBuilder("movie1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForInvalidDescription() {
        var builder = new Movie.MovieBuilder("movie1", episode);

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");
    }


    @Test
    public void testWithReleaseDateThrowsInvalidContentExceptionForInvalidReleaseDate() {
        var builder = new Movie.MovieBuilder("movie1", episode);

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be null");

        assertThatThrownBy(() -> builder.withReleaseDate(LocalDate.now().plusDays(10)))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be in the future");
    }

    @Test
    public void testGetDurationMinutesRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("movie1", episode).build();
        assertThat(movie.getDurationMinutes()).isEqualTo(episode.getDurationMinutes());
    }

    @Test
    public void testPlayRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("movie1", episode)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        ViewingProgress progress = movie.play(user, ViewingProgress.empty(), 60);

        assertThat(progress.getStartingEpisode()).isEqualTo(episode);
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(60);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(60);
        assertThat(progress.isCompleted(movie)).isFalse();

        progress = movie.play(user, progress, 60);
        assertThat(progress.getStartingEpisode()).isEqualTo(episode);
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(60);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(120);
        assertThat(progress.isCompleted(movie)).isTrue();
    }

    @Test
    public void testPlayThrowsNullPointerExceptionForNullArguments() {
        Movie movie = new Movie.MovieBuilder("movie1", episode).build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> movie.play(null, ViewingProgress.empty(), 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");

        assertThatThrownBy(() -> movie.play(user, null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Current progress cannot be null");
    }

    @Test
    public void testPlayThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        Movie movie = new Movie.MovieBuilder("movie1", episode).build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> movie.play(user, ViewingProgress.empty(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch cannot be negative");
    }

    @Test
    public void testPlayThrowsAccessDeniedExceptionForNoAccessUser() {
        Movie movie = new Movie.MovieBuilder("movie1", episode)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password")
                .build();

        assertThatThrownBy(() -> movie.play(user, ViewingProgress.empty(), 60))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(String.format("User %s does not have access to content '%s'.",
                        user.getUsername(), movie.getTitle()));
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie1", episode).build();

        assertThat(movie1).isEqualTo(movie2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie2", episode).build();

        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie2 = new Movie.MovieBuilder("movie1", episode).build();
        Movie movie3 = new Movie.MovieBuilder("movie2", episode).build();

        assertThat(movie1.hashCode()).isEqualTo(movie2.hashCode());
        assertThat(movie1.hashCode()).isNotEqualTo(movie3.hashCode());
        assertThat(movie2.hashCode()).isNotEqualTo(movie3.hashCode());
    }
}