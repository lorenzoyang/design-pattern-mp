package com.github.lorenzoyang.streamingplatform.content.video;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidVideoPathException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class VideoTest {
    @Test
    public void testConstructorCreatesVideoWithDefaultResolution() {
        var filePath = "path/to/video.mp4";
        double duration = 120.0;
        var video = new Video(filePath, duration);

        assertThat(video.getFilePath()).isEqualTo(filePath);
        assertThat(video.getDurationMinutes()).isEqualTo(duration);
        assertThat(video.getResolution()).isEqualTo(VideoResolution.HD);
    }

    @Test
    public void testConstructorThrowsInvalidVideoPathExceptionForInvalidFilePath() {
        assertThatThrownBy(() -> new Video(null, 120.0))
                .isInstanceOf(InvalidVideoPathException.class)
                .hasMessage("File path cannot be null or blank");

        assertThatThrownBy(() -> new Video("   ", 120.0))
                .isInstanceOf(InvalidVideoPathException.class)
                .hasMessage("File path cannot be null or blank");
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForInvalidDuration() {
        assertThatThrownBy(() -> new Video("path/to/video.mp4", 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be positive and non-zero");

        assertThatThrownBy(() -> new Video("path/to/video.mp4", -1.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Duration must be positive and non-zero");
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullResolution() {
        assertThatThrownBy(() -> new Video("path/to/video.mp4", 120.0, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Video resolution cannot be null");
    }

    @Test
    public void testEqualsReturnsTrueForSameFilePath() {
        var video1 = new Video("path/to/video.mp4", 120.0);
        var video2 = new Video("path/to/video.mp4", 120.0);

        assertThat(video1).isEqualTo(video2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentFilePath() {
        var video1 = new Video("path/to/video1.mp4", 120.0);
        var video2 = new Video("path/to/video2.mp4", 120.0);

        assertThat(video1).isNotEqualTo(video2);
    }

    @Test
    public void testHashCodeIsBasedOnFilePath() {
        var video1 = new Video("videos/sample1.mp4", 90.0, VideoResolution.HD);
        var video2 = new Video("videos/sample1.mp4", 120.0, VideoResolution.SD);
        var video3 = new Video("videos/sample2.mp4", 90.0, VideoResolution.HD);

        assertThat(video1.hashCode()).isEqualTo(video2.hashCode());

        assertThat(video1.hashCode()).isNotEqualTo(video3.hashCode());
        assertThat(video2.hashCode()).isNotEqualTo(video3.hashCode());
    }
}