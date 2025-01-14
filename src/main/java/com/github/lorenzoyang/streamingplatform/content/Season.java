package com.github.lorenzoyang.streamingplatform.content;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * package-private class representing a season of a TV series
 */
class Season {
    private final int seasonNumber;
    private final List<Episode> episodes;

    private final int totalDurationMinutes;

    public Season(int seasonNumber, List<Episode> episodes) {
        this.seasonNumber = seasonNumber;
        this.episodes = Objects.requireNonNull(episodes, "Episodes cannot be null");

        this.totalDurationMinutes = episodes.stream()
                .mapToInt(Episode::getDurationMinutes)
                .sum();
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public Iterator<Episode> getEpisodes() {
        return episodes.iterator();
    }

    public int getDurationMinutes() {
        return totalDurationMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return seasonNumber == season.seasonNumber &&
                totalDurationMinutes == season.totalDurationMinutes &&
                Objects.equals(episodes, season.episodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonNumber, episodes, totalDurationMinutes);
    }
}
