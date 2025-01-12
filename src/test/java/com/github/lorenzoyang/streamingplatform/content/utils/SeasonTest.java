package com.github.lorenzoyang.streamingplatform.content.utils;

import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class SeasonTest {
    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForNegativeOrZeroSeasonNumber() {
        assertThatThrownBy(() -> new Season(0, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season number must be positive and non-zero");
        assertThatThrownBy(() -> new Season(-1, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season number must be positive and non-zero");
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullEpisodes() {
        assertThatThrownBy(() -> new Season(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episodes cannot be null");
    }

    @Test
    public void testTotalDurationMinutesRunsCorrectly() {
        var episode1 = new Episode("Title1", 1, 60);
        var episode2 = new Episode("Title2", 2, 30);
        var season = new Season(1, List.of(episode1, episode2));
        assertThat(season.getDurationMinutes())
                .isEqualTo(episode1.getDurationMinutes() + episode2.getDurationMinutes());
    }

    @Test
    public void testEqualsReturnsTrueForSameFields() {
        var episode1 = new Episode("Title1", 1, 60);
        var episode2 = new Episode("Title2", 2, 30);
        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(1, List.of(episode1, episode2));

        assertThat(season1).isEqualTo(season2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentFields() {
        var episode1 = new Episode("Title1", 1, 60);
        var episode2 = new Episode("Title2", 2, 30);

        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(2, List.of(episode1, episode2));
        assertThat(season1).isNotEqualTo(season2);


        var season3 = new Season(1, List.of(episode1));
        assertThat(season1).isNotEqualTo(season3);
    }

    @Test
    public void testHashCodeIsBasedOnAllFields() {
        var episode1 = new Episode("Title1", 1, 60);
        var episode2 = new Episode("Title2", 2, 30);

        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(1, List.of(episode1, episode2));
        assertThat(season1.hashCode()).isEqualTo(season2.hashCode());

        var season3 = new Season(2, List.of(episode1, episode2));
        assertThat(season1.hashCode()).isNotEqualTo(season3.hashCode());

        var season4 = new Season(1, List.of(episode1));
        assertThat(season1.hashCode()).isNotEqualTo(season4.hashCode());
    }
}