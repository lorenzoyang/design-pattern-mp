package com.github.lorenzoyang.streamingplatform.content;

import java.util.Objects;

public class Video {
    private static long idCounter = 0;

    private final long id;
    private final String filePath;
    private final VideoFormat format;
    private final VideoResolution resolution;

    public Video(String filePath) {
        this(filePath, VideoFormat.MP4, VideoResolution.HD);
    }

    public Video(String filePath, VideoFormat format, VideoResolution resolution) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or blank");
        }
        this.id = idCounter++;
        this.filePath = filePath;
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
