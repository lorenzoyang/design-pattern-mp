package com.github.lorenzoyang.streamingplatform.content;

import java.util.Objects;

public class Episode {
    private final int episodeNumber;
    private final Video video;

    public Episode(int episodeNumber, Video video) {
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive and non-zero");
        }
        this.episodeNumber = episodeNumber;
        this.video = video;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }
    
    public Video getVideo() {
        return video;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return Objects.equals(getVideo(), episode.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVideo());
    }
}
