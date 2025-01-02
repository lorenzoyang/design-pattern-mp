package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

public class ContentProgress {
    private final Video currentVideo;
    private final double totalWatchedMinutes;

    public ContentProgress(Video currentVideo, double totalWatchedMinutes) {
        if (totalWatchedMinutes < 0) {
            throw new IllegalArgumentException("Total watched minutes cannot be negative");
        }
        if (currentVideo == null && totalWatchedMinutes != 0) {
            throw new IllegalArgumentException("Total watched minutes must be 0 if current video is null");
        }
        this.currentVideo = currentVideo;
        this.totalWatchedMinutes = totalWatchedMinutes;
    }

    public Video getCurrentVideo() {
        return currentVideo;
    }

    public double getTotalWatchedMinutes() {
        return totalWatchedMinutes;
    }

    public boolean isCompleted(Content content) {
        return totalWatchedMinutes >= content.getDurationMinutes();
    }

    // For creating initial progress
    public static ContentProgress initial() {
        return new ContentProgress(null, 0);
    }
}
