package com.github.lorenzoyang.streamingplatform.content.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Season {
    private final int seasonNumber;
    private final List<Episode> episodes;

    private final double totalDurationMinutes;

    public Season(int seasonNumber, List<Episode> episodes) {
        if (seasonNumber <= 0) {
            throw new IllegalArgumentException("Season number must be positive and non-zero");
        }
        this.seasonNumber = seasonNumber;

        this.episodes = Objects.requireNonNull(episodes, "Episodes cannot be null");

        this.totalDurationMinutes = episodes.stream()
                .mapToDouble(Episode::getDurationMinutes)
                .sum();
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public Iterator<Episode> getEpisodes() {
        return episodes.iterator();
    }

    public double getDurationMinutes() {
        return totalDurationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return getSeasonNumber() == season.getSeasonNumber() &&
                Double.compare(totalDurationMinutes, season.totalDurationMinutes) == 0 &&
                Objects.equals(episodes, season.episodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeasonNumber(), episodes, totalDurationMinutes);
    }
}
