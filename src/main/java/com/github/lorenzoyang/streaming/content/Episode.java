package com.github.lorenzoyang.streaming.content;

public class Episode {
    private final int episodeNumber;
    private final int durationSeconds;
    private final Video video;

    public Episode(int episodeNumber, int durationSeconds, Video video) {
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive and non-zero");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.episodeNumber = episodeNumber;
        this.durationSeconds = durationSeconds;
        this.video = video;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public Video getVideo() {
        return video;
    }
}
