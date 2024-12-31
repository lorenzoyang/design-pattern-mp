package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.VideoResolution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static java.lang.System.lineSeparator;

public class TVSeries extends Content {
    private final List<Episode> episodes;
    private final VideoResolution requiredResolution;
    private final int season; // optional

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.episodes = builder.episodes;
        this.requiredResolution = builder.requiredResolution;
        this.season = builder.season;
    }

    public Iterator<Episode> getEpisodes() {
        return episodes.iterator();
    }

    public VideoResolution getRequiredResolution() {
        return requiredResolution;
    }

    public int getSeason() {
        return season;
    }

    @Override
    protected String getDetailedContentInfo() {
        var sb = new StringBuilder();
        sb.append("\t").append("Season: ").append(season).append(lineSeparator());
        sb.append("\t").append("Resolution of episodes: ").append(requiredResolution).append(lineSeparator());
        episodes.forEach(episode -> {
            sb.append("\t").append("Episode ").append(episode.getEpisodeNumber()).append(": ")
                    .append(episode.getTitle()).append(lineSeparator());
        });
        sb.append("\t").append("Total episodes: ").append(episodes.size());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TVSeries tvSeries = (TVSeries) o;
        return Objects.equals(getEpisodes(), tvSeries.getEpisodes());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getEpisodes());
    }


    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final List<Episode> episodes = new ArrayList<>(); // empty list by default
        private final VideoResolution requiredResolution;
        private int season;

        public TVSeriesBuilder(String title, VideoResolution requiredResolution) {
            super(title);
            this.requiredResolution = Objects.requireNonNull(requiredResolution, "Required resolution cannot be null");
            this.season = 1; // default value
        }

        public TVSeriesBuilder addEpisode(Episode episode) {
            if (episode == null) {
                throw new IllegalArgumentException("Episode cannot be null");
            }
            if (episode.getVideo().getResolution() != requiredResolution) {
                throw new IllegalArgumentException("Episode resolution does not match the required resolution");
            }
            if (episodes.contains(episode)) {
                throw new IllegalArgumentException("Episode already added");
            }
            this.episodes.add(episode);
            return this;
        }

        public TVSeriesBuilder withSeason(int season) {
            if (season <= 0) {
                throw new IllegalArgumentException("Season must be positive and non-zero");
            }
            this.season = season;
            return this;
        }

        @Override
        protected TVSeriesBuilder self() {
            return this;
        }

        @Override
        public TVSeries build() {
            return new TVSeries(this);
        }
    }
}
