package com.github.lorenzoyang.streamingplatform.content.video;

public enum VideoResolution {
    SD(640, 480),
    HD(1280, 720),
    FHD(1920, 1080),
    UHD(3840, 2160);

    private final int width;
    private final int height;

    VideoResolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return name() + "(" + width + "x" + height + ")";
    }
}
