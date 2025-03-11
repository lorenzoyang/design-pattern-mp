package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidSeasonException;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class Season {
    private final int seasonNumber;
    private final List<Episode> episodes;
    private final int durationInMinutes;

    public Season(int seasonNumber, List<Episode> episodes) {
        if (seasonNumber < 1) {
            throw new InvalidSeasonException("Season number must be a positive integer greater than 0");
        }
        this.seasonNumber = seasonNumber;

        Objects.requireNonNull(episodes, "Episodes cannot be null");
        if (!IntStream.range(0, episodes.size())
                .allMatch(i -> episodes.get(i).getEpisodeNumber() == i + 1)) {
            throw new InvalidSeasonException("Episodes must be in order");
        }
        this.episodes = List.copyOf(episodes);

        this.durationInMinutes = episodes.stream()
                .mapToInt(Episode::getDurationInMinutes)
                .sum();
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public List<Episode> getEpisodes() {
        return episodes; // already immutable because of List.copyOf
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Season season = (Season) o;
        return seasonNumber == season.seasonNumber &&
                durationInMinutes == season.durationInMinutes &&
                Objects.equals(episodes, season.episodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seasonNumber, episodes, durationInMinutes);
    }
}
