package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidSeasonException;

import java.util.*;
import java.util.stream.IntStream;

public class Season implements Iterable<Episode> {
    private final int seasonNumber;
    private final Collection<Episode> episodes;
    private final int durationInMinutes;

    public Season(int seasonNumber, List<Episode> episodes) {
        if (seasonNumber < 1) {
            throw new InvalidSeasonException("Season number must be a positive integer greater than 0");
        }
        this.seasonNumber = seasonNumber;

        Objects.requireNonNull(episodes, "Episodes cannot be null");
        if (!IntStream.range(0, episodes.size())
                .allMatch(i -> episodes.get(i).getEpisodeNumber() == (i + 1))) {
            throw new InvalidSeasonException("Episodes must be consecutive and start from 1");
        }
        this.episodes = new ArrayList<>(episodes);

        this.durationInMinutes = episodes.stream()
                .mapToInt(Episode::getDurationInMinutes)
                .sum();
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public int getEpisodesCount() {
        return episodes.size();
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Season other = (Season) obj;
        return (seasonNumber == other.seasonNumber) &&
                (durationInMinutes == other.durationInMinutes) &&
                Objects.equals(episodes, other.episodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonNumber, episodes, durationInMinutes);
    }

    @Override
    public Iterator<Episode> iterator() {
        return episodes.iterator();
    }
}
