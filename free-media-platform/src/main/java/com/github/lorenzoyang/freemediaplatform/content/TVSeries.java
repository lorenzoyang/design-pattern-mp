package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.freemediaplatform.utils.ContentVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TVSeries extends Content {
    private final List<Season> seasons;
    private final int durationInMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.seasons = new ArrayList<>(builder.seasons);
        this.durationInMinutes = seasons.stream()
                .mapToInt(Season::getDurationInMinutes)
                .sum();
    }

    public int getSeasonsCount() {
        return seasons.size();
    }

    public List<Season> getSeasons() {
        return Collections.unmodifiableList(seasons);
    }

    @Override
    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return visitor.visitTVSeries(this);
    }

    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final List<Season> seasons;
        private int lastSeasonNumber;

        public TVSeriesBuilder(String title) {
            super(title);
            this.seasons = new ArrayList<>();
            this.lastSeasonNumber = 0;
        }

        public TVSeriesBuilder withSeason(Season season) {
            Objects.requireNonNull(season, "Season cannot be null");

            if (season.getSeasonNumber() != lastSeasonNumber + 1) {
                throw new InvalidContentException("TV series season numbers must be consecutive");
            }

            seasons.add(season);
            lastSeasonNumber = season.getSeasonNumber();
            return this;
        }

        @Override
        protected TVSeriesBuilder self() {
            return this;
        }

        @Override
        public TVSeries build() {
            if (seasons.isEmpty()) {
                throw new InvalidContentException("A TV series must have at least one season");
            }
            return new TVSeries(this);
        }
    }
}
