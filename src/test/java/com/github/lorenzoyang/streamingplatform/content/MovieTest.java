package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MovieTest {
    private Video video;

    @Before
    public void setUp() {
        this.video = new Video("video", 120);
    }

    @Test
    public void testMovieBuilderThrowsInvalidContentExceptionForInvalidTitle() {
        assertThatThrownBy(() -> new Movie.MovieBuilder(null, video))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");

        assertThatThrownBy(() -> new Movie.MovieBuilder("    ", video))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");
    }

    @Test
    public void testMovieBuilderThrowsNullPointerExceptionForNullVideo() {
        assertThatThrownBy(() -> new Movie.MovieBuilder("movie1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Video cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForInvalidDescription() {
        var builder = new Movie.MovieBuilder("movie1", video);

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");
    }


    @Test
    public void testWithReleaseDateThrowsInvalidContentExceptionForInvalidReleaseDate() {
        var builder = new Movie.MovieBuilder("movie1", video);

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be null");

        assertThatThrownBy(() -> builder.withReleaseDate(LocalDate.now().plusDays(1)))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be in the future");
    }

    @Test
    public void testMovieBuilderCreatesMovieWithValidData() {
        var releaseDate = LocalDate.now();
        Movie movie = new Movie.MovieBuilder("movie1", video)
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .build();

        assertThat(movie.getTitle()).isEqualTo("movie1");
        assertThat(movie.getDescription()).isEqualTo("description");
        assertThat(movie.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(movie.getVideo()).isEqualTo(video);
        assertThat(movie.isFree()).isFalse();
    }

    @Test
    public void testGetDurationMinutesRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("movie1", video).build();
        assertThat(movie.getDurationMinutes()).isEqualTo(120);
    }

    @Test
    public void testPlayThrowsNullPointerExceptionForNullArguments() {
        Movie movie = new Movie.MovieBuilder("movie1", video).build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        assertThatThrownBy(() -> movie.play(null, ViewingProgress.empty(), 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");

        assertThatThrownBy(() -> movie.play(user, null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Current progress cannot be null");
    }

    @Test
    public void testPlayThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        Movie movie = new Movie.MovieBuilder("movie1", video).build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        assertThatThrownBy(() -> movie.play(user, ViewingProgress.empty(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch cannot be negative");
    }

    @Test
    public void testPlayThrowsAccessDeniedExceptionForNoAccessUser() {
        Movie movie = new Movie.MovieBuilder("movie1", video)
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
    public void testPlayRunsCorrectly() {
        Movie movie = new Movie.MovieBuilder("movie1", video)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        ViewingProgress progress = movie.play(user, ViewingProgress.empty(), 60);

        assertThat(progress.getStartingVideo()).isEqualTo(video);
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(60);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(60);
        assertThat(progress.isCompleted(movie)).isFalse();
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", video).build();
        Movie movie2 = new Movie.MovieBuilder("movie1", video).build();

        assertThat(movie1).isEqualTo(movie2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", video).build();
        Movie movie2 = new Movie.MovieBuilder("movie2", video).build();

        assertThat(movie1).isNotEqualTo(movie2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        Movie movie1 = new Movie.MovieBuilder("movie1", video).build();
        Movie movie2 = new Movie.MovieBuilder("movie1", video).build();
        Movie movie3 = new Movie.MovieBuilder("movie2", video).build();

        assertThat(movie1.hashCode()).isEqualTo(movie2.hashCode());
        assertThat(movie1.hashCode()).isNotEqualTo(movie3.hashCode());
        assertThat(movie2.hashCode()).isNotEqualTo(movie3.hashCode());
    }
}