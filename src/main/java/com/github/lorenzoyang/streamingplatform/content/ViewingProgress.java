package com.github.lorenzoyang.streamingplatform.content;

public class ViewingProgress {
    private final Episode startingEpisode;
    private final double currentViewingDuration;
    private final double totalViewingDuration;

    public static ViewingProgress empty() {
        return new ViewingProgress(null, 0, 0);
    }

    public static ViewingProgress of(Episode startingEpisode, double currentViewingDuration, double totalViewingDuration) {
        if (currentViewingDuration < 0 || totalViewingDuration < 0) {
            throw new IllegalArgumentException("Viewing durations cannot be negative.");
        }
        if (currentViewingDuration > totalViewingDuration) {
            throw new IllegalArgumentException("Current viewing duration cannot exceed total viewing duration.");
        }
        return startingEpisode == null ? empty()
                : new ViewingProgress(startingEpisode, currentViewingDuration, totalViewingDuration);
    }

    private ViewingProgress(Episode startingEpisode, double currentViewingDuration, double totalViewingDuration) {
        this.startingEpisode = startingEpisode;
        this.currentViewingDuration = currentViewingDuration;
        this.totalViewingDuration = totalViewingDuration;
    }

    public Episode getStartingEpisode() {
        return startingEpisode;
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
