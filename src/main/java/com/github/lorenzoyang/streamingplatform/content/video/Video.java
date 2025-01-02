package com.github.lorenzoyang.streamingplatform.content.video;

import java.util.Objects;

public class Video {
    private static long idCounter = 0;

    private final long id;
    private final String filePath;
    private final double durationMinutes;
    private final VideoFormat format;
    private final VideoResolution resolution;


    public Video(String filePath, double durationMinutes) {
        this(filePath, durationMinutes, VideoFormat.MP4, VideoResolution.HD);
    }

    public Video(String filePath, double durationMinutes, VideoFormat format, VideoResolution resolution) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or blank");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.id = idCounter++;
        this.filePath = filePath;
        this.durationMinutes = durationMinutes;
        this.format = format;
        this.resolution = resolution;
    }

    public long getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public VideoFormat getFormat() {
        return format;
    }

    public VideoResolution getResolution() {
        return resolution;
    }

    public double getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return getId() == video.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
