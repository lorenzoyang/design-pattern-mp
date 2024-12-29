package com.github.lorenzoyang.streaming.content;

public class Video {
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
        this.filePath = filePath;
        this.format = format;
        this.resolution = resolution;
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
    public String toString() {
        return "Video{" +
                "filePath='" + filePath + '\'' +
                ", format=" + format +
                ", resolution=" + resolution +
                '}';
    }
}
