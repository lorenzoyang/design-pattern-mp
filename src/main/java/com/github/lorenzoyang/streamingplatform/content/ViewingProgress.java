package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

public class ViewingProgress {
    private final Video startingVideo;
    private final double currentViewingDuration;
    private final double totalViewingDuration;

    public static ViewingProgress empty() {
        return new ViewingProgress(null, 0, 0);
    }

    public static ViewingProgress of(Video startingVideo, double currentViewingDuration, double totalViewingDuration) {
        if (currentViewingDuration < 0 || totalViewingDuration < 0) {
            throw new IllegalArgumentException("Viewing durations must be non-negative.");
        }
        if (currentViewingDuration > totalViewingDuration) {
            throw new IllegalArgumentException("Current viewing duration cannot exceed total viewing duration.");
        }
        return startingVideo == null ? empty()
                : new ViewingProgress(startingVideo, currentViewingDuration, totalViewingDuration);
    }

    private ViewingProgress(Video startingVideo, double currentViewingDuration, double totalViewingDuration) {
        this.startingVideo = startingVideo;
        this.currentViewingDuration = currentViewingDuration;
        this.totalViewingDuration = totalViewingDuration;
    }

    public Video getStartingVideo() {
        return startingVideo;
    }

    public double getCurrentViewingDuration() {
        return currentViewingDuration;
    }

    public double getTotalViewingDuration() {
        return totalViewingDuration;
    }

    public boolean isCompleted(Content content) {
        return getTotalViewingDuration() >= content.getDurationMinutes();
    }
}
