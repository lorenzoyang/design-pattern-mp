package com.github.lorenzoyang.streamingplatform.content;

import java.util.Objects;

public class Episode {
    private final int episodeNumber;
    private final int durationMinutes;
    private final Video video;

    public Episode(int episodeNumber, int durationSeconds, Video video) {
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive and non-zero");
        }
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.episodeNumber = episodeNumber;
        this.durationMinutes = durationSeconds;
        this.video = video;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public Video getVideo() {
        return video;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(getVideo(), episode.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVideo());
    }
}
