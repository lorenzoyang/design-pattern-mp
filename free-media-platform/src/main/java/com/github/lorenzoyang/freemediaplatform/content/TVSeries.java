package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.freemediaplatform.utils.ContentVisitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class TVSeries extends Content implements Iterable<Season> {
    private final Collection<Season> seasons;
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

    @Override
    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return visitor.visitTVSeries(this);
    }

    @Override
    public Iterator<Season> iterator() {
        return seasons.iterator();
    }

    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final Collection<Season> seasons = new ArrayList<>();
        private int lastSeasonNumber = 0;

        public TVSeriesBuilder(String title, Season season) {
            super(title);
            withSeason(season);
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
            return new TVSeries(this);
        }
    }
}
