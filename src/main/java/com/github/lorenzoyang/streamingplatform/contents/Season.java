package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidSeasonException;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Season {
    private final int seasonNumber;
    private final List<Episode> episodes;
    private final int totalDurationMinutes;

    public Season(int seasonNumber, List<Episode> episodes) {
        if (seasonNumber < 1) {
            throw new InvalidSeasonException("Season number must be a positive integer greater than 0.");
        }
        this.seasonNumber = seasonNumber;

        this.episodes = Objects.requireNonNull(episodes, "Episodes cannot be null");
        if (!IntStream.range(0, episodes.size())
                .allMatch(i -> episodes.get(i).getEpisodeNumber() == i + 1)) {
            throw new InvalidSeasonException("Episodes must be in order");
        }

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
