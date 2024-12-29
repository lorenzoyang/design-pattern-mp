package com.github.lorenzoyang.streamingplatform.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class TVSeries extends Content {
    private final int season;
    private final List<Episode> episodes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.season = builder.season;
        this.episodes = builder.episodes;
    }

    public int getSeason() {
        return season;
    }

    public Iterator<Episode> getEpisodes() {
        return episodes.iterator();
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
        private final int season;
        private final List<Episode> episodes = new ArrayList<>(); // empty list by default

        public TVSeriesBuilder(String title, int season) {
            super(title);
            if (season <= 0) {
                throw new IllegalArgumentException("Season must be positive and non-zero");
            }
            this.season = season;
        }

        public TVSeriesBuilder addEpisode(Episode episode) {
            if (episode == null) {
                throw new IllegalArgumentException("Episode cannot be null");
            }
            if (episodes.contains(episode)) {
                throw new IllegalArgumentException("Episode already added");
            }
            this.episodes.add(episode);
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
