package com.github.lorenzoyang.streaming.content;

public class Video {
    private final String filePath;
    private VideoFormat format;
    private VideoResolution resolution;

    public Video(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("File path cannot be null or blank");
        }
        this.filePath = filePath;
        this.format = VideoFormat.MP4;
        this.resolution = VideoResolution.HD;
    }

    public Video format(VideoFormat format) {
        this.format = format;
        return this;
    }

    public Video resolution(VideoResolution resolution) {
        this.resolution = resolution;
        return this;
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
