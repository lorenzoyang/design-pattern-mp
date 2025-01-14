package com.github.lorenzoyang.streamingplatform.content;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class ViewingProgressTest {
    private Episode episode;

    @Before
    public void setUp() {
        this.episode = new Episode(1, 60);
    }

    @Test
    public void testOfCreatesViewingProgressWithValidArguments() {
        int currentViewingDuration = 30, totalViewingDuration = 50;
        ViewingProgress viewingProgress = ViewingProgress.of(episode, currentViewingDuration, totalViewingDuration);

        assertThat(viewingProgress.getStartingEpisode()).isEqualTo(episode);
        assertThat(viewingProgress.getCurrentViewingDuration()).isEqualTo(currentViewingDuration);
        assertThat(viewingProgress.getTotalViewingDuration()).isEqualTo(totalViewingDuration);
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForNegativeViewingDurations() {
        assertThatThrownBy(() -> ViewingProgress.of(episode, -1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Viewing durations cannot be negative.");

        assertThatThrownBy(() -> ViewingProgress.of(episode, 0, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Viewing durations cannot be negative.");

        assertThatThrownBy(() -> ViewingProgress.of(episode, -1, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Viewing durations cannot be negative.");
    }

    @Test
    public void testOfThrowsIllegalArgumentExceptionForCurrentViewingDurationExceedingTotalViewingDuration() {
        assertThatThrownBy(() -> ViewingProgress.of(episode, 1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Current viewing duration cannot exceed total viewing duration.");
    }

    @Test
    public void testEmptyCreatesViewingProgressCorrectly() {
        ViewingProgress viewingProgress = ViewingProgress.empty();

        assertThat(viewingProgress.getStartingEpisode()).isNull();
        assertThat(viewingProgress.getCurrentViewingDuration()).isEqualTo(0);
        assertThat(viewingProgress.getTotalViewingDuration()).isEqualTo(0);
    }

    @Test
    public void testOfCreatesEmptyViewingProgressForNullEpisode() {
        ViewingProgress viewingProgress = ViewingProgress.of(null, 1, 1);

        assertThat(viewingProgress.getStartingEpisode()).isNull();
        assertThat(viewingProgress.getCurrentViewingDuration()).isEqualTo(0);
        assertThat(viewingProgress.getTotalViewingDuration()).isEqualTo(0);
    }

    @Test
    public void testIsCompletedReturnsTrueForCompletedViewingProgress() {
        Content mockContent = new MockContent("Mock Content") {
            @Override
            public int getDurationMinutes() {
                return episode.getDurationMinutes();
            }
        };
        ViewingProgress progress = ViewingProgress.of(episode, 10, episode.getDurationMinutes());

        assertThat(progress.isCompleted(mockContent)).isTrue();
    }

    @Test
    public void testIsCompletedReturnsFalseForIncompleteViewingProgress() {
        Content mockContent = new MockContent("Mock Content") {
            @Override
            public int getDurationMinutes() {
                return episode.getDurationMinutes();
            }
        };
        ViewingProgress progress = ViewingProgress.of(
                episode,
                10,
                episode.getDurationMinutes() - 5
        );

        assertThat(progress.isCompleted(mockContent)).isFalse();
    }
}