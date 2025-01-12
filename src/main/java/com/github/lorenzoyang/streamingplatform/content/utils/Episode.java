package com.github.lorenzoyang.streamingplatform.content.utils;

import java.util.Objects;

public class Episode {
    private final String title;
    private final int episodeNumber;
    private final double durationMinutes;

    public Episode(String title, int episodeNumber, double durationMinutes) {
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

    public double getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return getEpisodeNumber() == episode.getEpisodeNumber() && Double.compare(getDurationMinutes(), episode.getDurationMinutes()) == 0 && Objects.equals(getTitle(), episode.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getEpisodeNumber(), getDurationMinutes());
    }
}
