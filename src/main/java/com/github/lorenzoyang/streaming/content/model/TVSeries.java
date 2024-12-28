package com.github.lorenzoyang.streaming.content.model;

import com.github.lorenzoyang.streaming.content.Content;
import com.github.lorenzoyang.streaming.content.Episode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TVSeries extends Content {
    private final int season;
    private final List<Episode> episodes;

    private TVSeries(String title, String description, LocalDate releaseDate, int season, List<Episode> episodes) {
        super(title, description, releaseDate);
        this.season = season;
        this.episodes = episodes;
    }

    public int getSeason() {
        return season;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public static class TVSeriesBuilder extends Content.Builder<TVSeriesBuilder> {
        private final int season;
        private final List<Episode> episodes;

        public TVSeriesBuilder(String title, int season) {
            super(title);
            if (season <= 0) {
                throw new IllegalArgumentException("Season must be positive and non-zero");
            }
            this.season = season;
            this.episodes = new ArrayList<>();
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
            return new TVSeries(title, description, releaseDate, season, episodes);
        }
    }
}
