package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

import java.util.Objects;

public class Episode {
    private final String title;
    private final int episodeNumber;
    private final Video video;

    public Episode(String title, int episodeNumber, Video video) {
        this.title = Objects.requireNonNull(title, "Title cannot be null");
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive and non-zero");
        }
        this.episodeNumber = episodeNumber;
        this.video = Objects.requireNonNull(video, "Video cannot be null");
    }

    public String getTitle() {
        return title;
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
