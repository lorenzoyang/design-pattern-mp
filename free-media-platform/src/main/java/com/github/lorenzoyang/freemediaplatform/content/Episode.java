package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidEpisodeException;

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
            throw new InvalidEpisodeException("Episode duration must be a positive integer greater than 0.");
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Episode other = (Episode) obj;
        return (episodeNumber == other.episodeNumber) && (durationInMinutes == other.durationInMinutes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeNumber, durationInMinutes);
    }
}
