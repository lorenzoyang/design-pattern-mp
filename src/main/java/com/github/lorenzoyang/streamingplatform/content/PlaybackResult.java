package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

public class PlaybackResult {
    private final Video startVideo;
    private final double startingPointVideo;
    private final double progress;
    private final double watchedMinutes;

    public PlaybackResult(Video startVideo, double startingPointVideo, double progress, double watchedMinutes) {
        this.startVideo = startVideo;
        this.startingPointVideo = startingPointVideo;
        this.progress = progress;
        this.watchedMinutes = watchedMinutes;
    }

    public Video getStartVideo() {
        return startVideo;
    }

    public double getStartingPointVideo() {
        return startingPointVideo;
    }

    public double getProgress() {
        return progress;
    }

    public double getWatchedMinutes() {
        return watchedMinutes;
    }
}
