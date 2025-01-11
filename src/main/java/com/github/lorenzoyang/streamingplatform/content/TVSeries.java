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
        this.totalDurationMinutes = episodes
                .stream()
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
    protected ViewingProgress playContent(ViewingProgress currentProgress, double timeToWatch) {
        int episodeIndex = 0;
        double totalWatchedTime = currentProgress.getTotalWatchedTime();
        for (Episode episode : episodes) {
            double episodeDuration = episode.getVideo().getDurationMinutes();
            if (totalWatchedTime < episodeDuration) {
                break;
            }
            totalWatchedTime -= episodeDuration;
            episodeIndex++;
        }

        totalWatchedTime = Math.min(currentProgress.getTotalWatchedTime() + timeToWatch, getDurationMinutes());

        return ViewingProgress.createWith(
                episodes.get(episodeIndex).getVideo(),
                totalWatchedTime - currentProgress.getTotalWatchedTime(),
                totalWatchedTime
        );
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TVSeries tvSeries = (TVSeries) o;
        return getSeason() == tvSeries.getSeason();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSeason());
    }

    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final VideoResolution requiredResolution;
        private final List<Episode> episodes;
        private int season;

        public TVSeriesBuilder(String title, VideoResolution requiredResolution) {
            super(title);
            this.requiredResolution = Objects.requireNonNull(
                    requiredResolution, "Required resolution cannot be null");
            this.episodes = new ArrayList<>();
            this.season = 1;
        }

        public TVSeriesBuilder withSeason(int season) {
            if (season <= 0) {
                throw new IllegalArgumentException("Season must be positive and non-zero");
            }
            this.season = season;
            return this;
        }

        public TVSeriesBuilder addEpisode(Episode episode) {
            Objects.requireNonNull(episode, "Episode cannot be null");
            if (episode.getVideo().getResolution() != requiredResolution) {
                throw new IllegalArgumentException("Episode resolution does not match the required resolution");
            }
            if (episodes.contains(episode)) {
                throw new IllegalArgumentException("Episode already added");
            }
            this.episodes.add(episode);
            return this;
        }

        public TVSeriesBuilder withEpisodes(List<Episode> episodes) {
            Objects.requireNonNull(episodes, "Episodes cannot be null");
            episodes.forEach(this::addEpisode);
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
