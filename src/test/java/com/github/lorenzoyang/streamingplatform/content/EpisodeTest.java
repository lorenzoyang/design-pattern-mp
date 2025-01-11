package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EpisodeTest {
    private Video video1;
    private Video video2;

    @Before
    public void setUp() {
        video1 = new Video("path/to/video1.mp4", 120.0);
        video2 = new Video("path/to/video2.mp4", 120.0);
    }
    
    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForInvalidEpisodeNumber() {
        assertThatThrownBy(() -> new Episode("Title", 0, video1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode number must be positive and non-zero");

        assertThatThrownBy(() -> new Episode("Title", -1, video1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode number must be positive and non-zero");
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullTitle() {
        assertThatThrownBy(() -> new Episode(null, 1, video1))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Title cannot be null");
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullVideo() {
        assertThatThrownBy(() -> new Episode("Title", 1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Video cannot be null");
    }

    @Test
    public void testEqualsReturnsTrueForSameVideo() {
        var episode1 = new Episode("Title1", 1, video1);
        var episode2 = new Episode("Title2", 2, video1);

        assertThat(episode1).isEqualTo(episode2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentVideo() {
        var episode1 = new Episode("Title1", 1, video1);
        var episode2 = new Episode("Title1", 1, video2);

        assertThat(episode1).isNotEqualTo(episode2);
    }

    @Test
    public void testHashCodeIsBasedOnVideo() {
        var episode1 = new Episode("Title1", 1, video1);
        var episode2 = new Episode("Title2", 2, video1);
        var episode3 = new Episode("Title1", 1, video2);

        assertThat(episode1.hashCode()).isEqualTo(episode2.hashCode());

        assertThat(episode1.hashCode()).isNotEqualTo(episode3.hashCode());
        assertThat(episode2.hashCode()).isNotEqualTo(episode3.hashCode());
    }
}