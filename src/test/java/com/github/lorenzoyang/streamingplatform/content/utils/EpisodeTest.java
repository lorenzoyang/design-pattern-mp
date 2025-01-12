package com.github.lorenzoyang.streamingplatform.content.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class EpisodeTest {
    @Test
    public void testConstructorThrowsNullPointerExceptionForNullTitle() {
        assertThatThrownBy(() -> new Episode(null, 1, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Title cannot be null");
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForNegativeOrZeroEpisodeNumber() {
        assertThatThrownBy(() -> new Episode("Title", 0, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode number must be positive and non-zero");

        assertThatThrownBy(() -> new Episode("Title", -1, 60))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode number must be positive and non-zero");
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForNegativeOrZeroDuration() {
        assertThatThrownBy(() -> new Episode("Title", 1, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be positive and non-zero");

        assertThatThrownBy(() -> new Episode("Title", 1, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be positive and non-zero");
    }

    @Test
    public void testEqualsReturnsTrueForSameFields() {
        var episode1 = new Episode("Title", 1, 60);
        var episode2 = new Episode("Title", 1, 60);

        assertThat(episode1).isEqualTo(episode2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentFields() {
        var episode1 = new Episode("Title", 1, 60);
        var episode2 = new Episode("Title", 2, 60);
        assertThat(episode1).isNotEqualTo(episode2);

        episode2 = new Episode("Title", 1, 120);
        assertThat(episode1).isNotEqualTo(episode2);
    }

    @Test
    public void testHashCodeIsBasedOnAllFields() {
        var episode1 = new Episode("Title", 1, 60);
        var episode2 = new Episode("Title", 1, 60);
        var episode3 = new Episode("Title", 2, 60);
        var episode4 = new Episode("Title", 1, 120);

        assertThat(episode1.hashCode()).isEqualTo(episode2.hashCode());
        assertThat(episode1.hashCode()).isNotEqualTo(episode3.hashCode());
        assertThat(episode1.hashCode()).isNotEqualTo(episode4.hashCode());
    }
}