package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

public class ContentProgress {
    private final Video startVideo;
    private final double watchedTime;
    private final double totalWatchedTime;

    public static ContentProgress initial() {
        return new ContentProgress(null, 0, 0);
    }

    public static ContentProgress of(Video startVideo, double watchedTime, double totalWatchedTime) {
        if (watchedTime < 0 || totalWatchedTime < 0) {
            throw new IllegalArgumentException("Watched time and total watched time must be positive");
        }
        if (watchedTime > totalWatchedTime) {
            throw new IllegalArgumentException("Watched time cannot be greater than total watched time");
        }
        if (startVideo == null && (watchedTime != 0 || totalWatchedTime != 0)) {
            throw new IllegalArgumentException("Watched time and total watched time must be 0 if start video is null");
        }
        return new ContentProgress(startVideo, watchedTime, totalWatchedTime);
    }

    private ContentProgress(Video startVideo, double watchedTime, double totalWatchedTime) {
        this.startVideo = startVideo;
        this.watchedTime = watchedTime;
        this.totalWatchedTime = totalWatchedTime;
    }

    public Video getStartVideo() {
        return startVideo;
    }

    public double getWatchedTime() {
        return watchedTime;
    }

    public double getTotalWatchedTime() {
        return totalWatchedTime;
    }

    public boolean isCompleted(Content content) {
        return getTotalWatchedTime() >= content.getDurationMinutes();
    }
}
