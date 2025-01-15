package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidEpisodeException;

import java.util.Objects;

public class Episode {
    private final int episodeNumber;
    private final int durationInMinutes;

    public Episode(int episodeNumber, int durationInMinutes) {
        if (episodeNumber <= 0) {
            throw new InvalidEpisodeException("Episode number must be a positive integer greater than 0.");
        }
        this.episodeNumber = episodeNumber;

        if (durationInMinutes <= 0) {
            throw new InvalidEpisodeException("Duration must be a positive integer greater than 0.");
        }
        this.durationInMinutes = durationInMinutes;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return episodeNumber == episode.episodeNumber && durationInMinutes == episode.durationInMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeNumber, durationInMinutes);
    }
}
