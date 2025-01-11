package com.github.lorenzoyang.streamingplatform.content.video;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidVideoPathException;

import java.util.Objects;

public class Video {
    private final String filePath;
    private final double durationMinutes;
    private final VideoResolution resolution;

    public Video(String filePath, double durationMinutes) {
        this(filePath, durationMinutes, VideoResolution.HD);
    }

    public Video(String filePath, double durationMinutes, VideoResolution resolution) {
        if (filePath == null || filePath.isBlank()) {
            throw new InvalidVideoPathException("File path cannot be null or blank");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.resolution = Objects.requireNonNull(resolution, "Video resolution cannot be null");
        this.filePath = filePath;
        this.durationMinutes = durationMinutes;
    }

    public String getFilePath() {
        return filePath;
    }

    public double getDurationMinutes() {
        return durationMinutes;
    }

    public VideoResolution getResolution() {
        return resolution;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(getFilePath(), video.getFilePath());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFilePath());
    }
}
