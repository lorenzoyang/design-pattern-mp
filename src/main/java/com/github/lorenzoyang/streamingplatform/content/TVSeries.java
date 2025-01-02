package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.VideoResolution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class TVSeries extends Content {
    private final List<Episode> episodes;
    private final VideoResolution requiredResolution;
    private final int season;

    private final double totalDurationMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.episodes = builder.episodes;
        this.requiredResolution = builder.requiredResolution;
        this.season = builder.season;
        this.totalDurationMinutes = episodes.stream()
                .mapToDouble(episode -> episode.getVideo().getDurationMinutes())
                .sum();
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
    public double getDurationMinutes() {
        return totalDurationMinutes;
    }

    @Override
    protected ContentProgress playContent(ContentProgress currentProgress, double timeToWatch) {
        int episodeIndex = 0;
        double totalWatchedMinutes = currentProgress.getTotalWatchedMinutes();
        while (episodeIndex < episodes.size()) {
            double episodeDuration = episodes.get(episodeIndex).getVideo().getDurationMinutes();
            if (totalWatchedMinutes < episodeDuration) {
                break;
            }
            totalWatchedMinutes -= episodeDuration;
        }
        totalWatchedMinutes = currentProgress.getTotalWatchedMinutes() + timeToWatch;
        if (totalWatchedMinutes >= getDurationMinutes()) {
            totalWatchedMinutes = getDurationMinutes();
        }
        return new ContentProgress(episodes.get(episodeIndex).getVideo(), totalWatchedMinutes);
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
        private final List<Episode> episodes = new ArrayList<>();
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
