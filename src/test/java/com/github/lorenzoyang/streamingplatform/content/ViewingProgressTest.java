package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ViewingProgressTest {
    private Video video;

    @Before
    public void setUp() {
        this.video = new Video("path/to/video.mp4", 160);
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForNegativeViewingDurations() {
        assertThatThrownBy(() -> ViewingProgress.of(video, -10, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Viewing durations must be non-negative.");

        assertThatThrownBy(() -> ViewingProgress.of(video, 10, -60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Viewing durations must be non-negative.");
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForCurrentViewingDurationGreaterThanTotalViewingDuration() {
        assertThatThrownBy(() -> ViewingProgress.of(video, 120, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current viewing duration cannot exceed total viewing duration.");
    }

    @Test
    public void testOfWithValidData() {
        double currentViewingDuration = 60, totalViewingDuration = 120;
        ViewingProgress progress = ViewingProgress.of(video, currentViewingDuration, totalViewingDuration);

        assertThat(progress.getStartingVideo()).isEqualTo(video);
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(currentViewingDuration);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(totalViewingDuration);
    }

    @Test
    public void testEmptyCreatesDefaultViewingProgress() {
        ViewingProgress progress = ViewingProgress.empty();

        assertThat(progress.getStartingVideo()).isNull();
        assertThat(progress.getCurrentViewingDuration()).isZero();
        assertThat(progress.getTotalViewingDuration()).isZero();
    }

    @Test
    public void testIsCompletedReturnsTrueForCompletedContent() {
        final double DURATION = video.getDurationMinutes();
        ViewingProgress progress1 = ViewingProgress.of(video, 160, DURATION);
        ViewingProgress progress2 = ViewingProgress.of(video, 10, DURATION);
        Content content = new Movie.MovieBuilder("movie1", video).build();

        assertThat(progress1.isCompleted(content)).isTrue();
        assertThat(progress2.isCompleted(content)).isTrue();
    }

    @Test
    public void testIsCompletedReturnsFalseForIncompleteContent() {
        final double DURATION = video.getDurationMinutes();
        ViewingProgress progress1 = ViewingProgress.of(video, 10, DURATION - 10);
        ViewingProgress progress2 = ViewingProgress.of(video, 0, DURATION - 20);
        Content content = new Movie.MovieBuilder("movie1", video).build();

        assertThat(progress1.isCompleted(content)).isFalse();
        assertThat(progress2.isCompleted(content)).isFalse();
    }
}