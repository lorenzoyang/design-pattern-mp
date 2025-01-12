//package com.github.lorenzoyang.streamingplatform.content.utils;
//
//import com.github.lorenzoyang.streamingplatform.content.Content;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//
//public class ViewingProgressTest {
//    private Episode episode;
//
//    @Before
//    public void setUp() {
//        this.episode = new Episode("episode1", 1, 26);
//    }
//
//    @Test
//    public void testOfThrowsIllegalArgumentExceptionForNegativeViewingDurations() {
//        assertThatThrownBy(() -> ViewingProgress.of(episode, -10, 60))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Viewing durations must be non-negative.");
//
//        assertThatThrownBy(() -> ViewingProgress.of(episode, 10, -60))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Viewing durations must be non-negative.");
//    }
//
//    @Test
//    public void testOfThrowsIllegalArgumentExceptionForCurrentViewingDurationGreaterThanTotalViewingDuration() {
//        assertThatThrownBy(() -> ViewingProgress.of(episode, 120, 60))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("Current viewing duration cannot exceed total viewing duration.");
//    }
//
//    @Test
//    public void testOfWithValidData() {
//        double currentViewingDuration = 60, totalViewingDuration = 120;
//        ViewingProgress progress = ViewingProgress.of(episode, currentViewingDuration, totalViewingDuration);
//
//        assertThat(progress.getStartingEpisode()).isEqualTo(episode);
//        assertThat(progress.getCurrentViewingDuration()).isEqualTo(currentViewingDuration);
//        assertThat(progress.getTotalViewingDuration()).isEqualTo(totalViewingDuration);
//    }
//
//    @Test
//    public void testEmptyCreatesDefaultViewingProgress() {
//        ViewingProgress progress = ViewingProgress.empty();
//
//        assertThat(progress.getStartingEpisode()).isNull();
//        assertThat(progress.getCurrentViewingDuration()).isZero();
//        assertThat(progress.getTotalViewingDuration()).isZero();
//    }
//
//    @Test
//    public void testIsCompletedReturnsTrueForCompletedContent() {
//        final double DURATION = episode.getDurationMinutes();
//        ViewingProgress progress1 = ViewingProgress.of(episode, 160, DURATION);
//        ViewingProgress progress2 = ViewingProgress.of(episode, 10, DURATION);
//        Content content = new Movie.MovieBuilder("movie1", video).build();
//
//        assertThat(progress1.isCompleted(content)).isTrue();
//        assertThat(progress2.isCompleted(content)).isTrue();
//    }
//
//    @Test
//    public void testIsCompletedReturnsFalseForIncompleteContent() {
//        final double DURATION = video.getDurationMinutes();
//        ViewingProgress progress1 = ViewingProgress.of(video, 10, DURATION - 10);
//        ViewingProgress progress2 = ViewingProgress.of(video, 0, DURATION - 20);
//        Content content = new Movie.MovieBuilder("movie1", video).build();
//
//        assertThat(progress1.isCompleted(content)).isFalse();
//        assertThat(progress2.isCompleted(content)).isFalse();
//    }
//}