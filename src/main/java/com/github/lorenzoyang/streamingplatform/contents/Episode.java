package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidEpisodeException;

import java.util.Objects;

public class Episode {
    private final int episodeNumber;
    private final int durationMinutes;

    public Episode(int episodeNumber, int durationMinutes) {
        if (episodeNumber <= 0) {
            throw new InvalidEpisodeException("Episode number must be a positive integer greater than 0.");
        }
        this.episodeNumber = episodeNumber;

        if (durationMinutes <= 0) {
            throw new InvalidEpisodeException("Duration must be a positive integer greater than 0.");
        }
        this.durationMinutes = durationMinutes;
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
        return episodeNumber == episode.episodeNumber && durationMinutes == episode.durationMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeNumber, durationMinutes);
    }
}
