package com.github.lorenzoyang.streamingplatform.content;

import java.util.Objects;

public class Episode {
    private final String title;
    private final int episodeNumber;
    private final int durationMinutes;

    public Episode(String title, int episodeNumber, int durationMinutes) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");

        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive and non-zero");
        }
        this.episodeNumber = episodeNumber;

        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be positive and non-zero");
        }
        this.durationMinutes = durationMinutes;
    }

    public String getTitle() {
        return title;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return episodeNumber == episode.episodeNumber &&
                durationMinutes == episode.durationMinutes &&
                Objects.equals(title, episode.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, episodeNumber, durationMinutes);
    }
}
