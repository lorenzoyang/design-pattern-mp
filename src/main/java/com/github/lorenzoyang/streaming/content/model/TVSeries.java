package com.github.lorenzoyang.streaming.content.model;

import com.github.lorenzoyang.streaming.content.Content;
import com.github.lorenzoyang.streaming.content.Episode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public List<Episode> getEpisodes() {
        return Collections.unmodifiableList(episodes);
    }

    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final int season;
        private final List<Episode> episodes = new ArrayList<>();

        public TVSeriesBuilder(String title, int season) {
            super(title);
            if (season <= 0) {
                throw new IllegalArgumentException("Season must be positive and non-zero");
            }
            this.season = season;
        }

        public TVSeriesBuilder addEpisode(Episode episode) {
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
