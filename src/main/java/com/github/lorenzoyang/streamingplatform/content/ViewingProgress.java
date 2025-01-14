package com.github.lorenzoyang.streamingplatform.content;

public class ViewingProgress {
    private final Episode startingEpisode;
    private final int currentViewingDuration;
    private final int totalViewingDuration;

    public static ViewingProgress empty() {
        return new ViewingProgress(null, 0, 0);
    }

    public static ViewingProgress of(Episode startingEpisode, int currentViewingDuration, int totalViewingDuration) {
        if (currentViewingDuration < 0 || totalViewingDuration < 0) {
            throw new IllegalArgumentException("Viewing durations cannot be negative.");
        }
        if (currentViewingDuration > totalViewingDuration) {
            throw new IllegalArgumentException("Current viewing duration cannot exceed total viewing duration.");
        }
        return startingEpisode == null ? empty()
                : new ViewingProgress(startingEpisode, currentViewingDuration, totalViewingDuration);
    }

    private ViewingProgress(Episode startingEpisode, int currentViewingDuration, int totalViewingDuration) {
        this.startingEpisode = startingEpisode;
        this.currentViewingDuration = currentViewingDuration;
        this.totalViewingDuration = totalViewingDuration;
    }

    public Episode getStartingEpisode() {
        return startingEpisode;
    }

    public int getCurrentViewingDuration() {
        return currentViewingDuration;
    }

    public int getTotalViewingDuration() {
        return totalViewingDuration;
    }

    public boolean isCompleted(Content content) {
        return getTotalViewingDuration() >= content.getDurationMinutes();
    }
}
