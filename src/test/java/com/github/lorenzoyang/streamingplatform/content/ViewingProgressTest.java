package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ViewingProgressTest {
    private Video video;
    private final double VIDEO_DURATION = 160;

    @Before
    public void setUp() {
        this.video = new Video("path/to/video.mp4", VIDEO_DURATION);
    }

    @Test
    public void testInitialCreatesDefaultViewingProgress() {
        ViewingProgress progress = ViewingProgress.initial();

        assertThat(progress.getStartVideo()).isNull();
        assertThat(progress.getWatchedTime()).isZero();
        assertThat(progress.getTotalWatchedTime()).isZero();
    }

    @Test
    public void testOfCreatesViewingProgressWithValidData() {
        double watchedTime = 60, totalWatchedTime = 120;
        ViewingProgress progress = ViewingProgress.of(video, watchedTime, totalWatchedTime);

        assertThat(progress.getStartVideo()).isEqualTo(video);
        assertThat(progress.getWatchedTime()).isEqualTo(watchedTime);
        assertThat(progress.getTotalWatchedTime()).isEqualTo(totalWatchedTime);
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForNegativeWatchedTime() {
        assertThatThrownBy(() -> ViewingProgress.of(video, -1, 120))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Watched time and total watched time must be positive");
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForNegativeTotalWatchedTime() {
        assertThatThrownBy(() -> ViewingProgress.of(video, 60, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Watched time and total watched time must be positive");
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForWatchedTimeGreaterThanTotalWatchedTime() {
        assertThatThrownBy(() -> ViewingProgress.of(video, 120, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Watched time cannot be greater than total watched time");
    }

    @Test
    public void testIsCompletedReturnsTrueForCompletedContent() {
        ViewingProgress progress1 = ViewingProgress.of(video, 160, VIDEO_DURATION);
        ViewingProgress progress2 = ViewingProgress.of(video, 10, VIDEO_DURATION);
        Content content = new Movie.MovieBuilder("movie1", video).build();

        assertThat(progress1.isCompleted(content)).isTrue();
        assertThat(progress2.isCompleted(content)).isTrue();
    }

    @Test
    public void testIsCompletedReturnsFalseForIncompleteContent() {
        ViewingProgress progress1 = ViewingProgress.of(video, 10, VIDEO_DURATION - 10);
        ViewingProgress progress2 = ViewingProgress.of(video, 0, VIDEO_DURATION - 20);
        Content content = new Movie.MovieBuilder("movie1", video).build();

        assertThat(progress1.isCompleted(content)).isFalse();
        assertThat(progress2.isCompleted(content)).isFalse();
    }
}