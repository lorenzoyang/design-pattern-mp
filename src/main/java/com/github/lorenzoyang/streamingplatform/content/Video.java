package com.github.lorenzoyang.streamingplatform.content;

import java.util.Objects;

public class Video {
    private static long idCounter = 0;

    private final long id;
    private final String filePath;
    private final VideoFormat format;
    private final VideoResolution resolution;
    private final long durationMinutes;

    public Video(String filePath, long durationMinutes) {
        this(filePath, VideoFormat.MP4, VideoResolution.HD, durationMinutes);
    }

    public Video(String filePath, VideoFormat format, VideoResolution resolution, long durationMinutes) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or blank");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.id = idCounter++;
        this.filePath = filePath;
        this.format = format;
        this.resolution = resolution;
        this.durationMinutes = durationMinutes;
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

    public long getDurationMinutes() {
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

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", format=" + format +
                ", resolution=" + resolution +
                '}';
    }
}
