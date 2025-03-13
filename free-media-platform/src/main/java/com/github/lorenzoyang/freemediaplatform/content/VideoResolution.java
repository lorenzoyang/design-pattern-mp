package com.github.lorenzoyang.freemediaplatform.content;

public enum VideoResolution {
    SD_480P("480p"),
    HD_720P("720p"),
    FULL_HD_1080P("1080p"),
    UHD_4K("4K");

    private final String displayName;

    VideoResolution(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
