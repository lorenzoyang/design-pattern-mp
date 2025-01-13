package com.github.lorenzoyang.streamingplatform.content;

import java.util.*;

public final class TVSeries extends Content {
    private final List<Season> seasons;

    private final double totalDurationMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);

        this.seasons = new ArrayList<>();
        builder.seasons.forEach(
                (seasonNumber, episodes) -> this.seasons.add(new Season(seasonNumber, episodes))
        );

        this.totalDurationMinutes = seasons.stream()
                .mapToDouble(Season::getDurationMinutes)
                .sum();
    }

    @Override
    public double getDurationMinutes() {
        return totalDurationMinutes;
    }

    @Override
    protected ViewingProgress playContent(ViewingProgress currentProgress, double timeToWatch) {
        Episode startingEpisode = getStartingEpisode(currentProgress.getTotalViewingDuration());

        // When we have empty season
        if (startingEpisode == null) {
            return ViewingProgress.empty();
        }

        double totalViewingDuration = Math.min(
                currentProgress.getTotalViewingDuration() + timeToWatch,
                this.getDurationMinutes()
        );

        return ViewingProgress.of(
                startingEpisode,
                totalViewingDuration - currentProgress.getTotalViewingDuration(),
                totalViewingDuration
        );
    }

    private Episode getStartingEpisode(double totalViewingDuration) {
        Episode startingEpisode = null;
        for (Season season : seasons) {
            Iterator<Episode> episodes = season.getEpisodes();
            while (episodes.hasNext()) {
                startingEpisode = episodes.next();
                if (totalViewingDuration < startingEpisode.getDurationMinutes()) {
                    return startingEpisode;
                }
                totalViewingDuration -= startingEpisode.getDurationMinutes();
            }
        }
        return startingEpisode;
    }

    // package-private getter for testing purposes
    List<Season> getSeasons() {
        return seasons;
    }

    public static class TVSeriesBuilder extends ContentBuilder<TVSeriesBuilder> {
        private final Map<Integer, List<Episode>> seasons;
        private int currentSeason;

        public TVSeriesBuilder(String title) {
            super(title);
            this.seasons = new LinkedHashMap<>();
            this.seasons.put(1, new ArrayList<>());
            this.currentSeason = 1;
        }

        public TVSeriesBuilder addSeason(int season) {
            if (season != currentSeason + 1) {
                throw new IllegalArgumentException("Invalid season number");
            }
            seasons.put(season, new ArrayList<>());
            currentSeason = season;
            return this;
        }

        public TVSeriesBuilder addSingleEpisode(int season, Episode episode) {
            if (!seasons.containsKey(season)) {
                throw new IllegalArgumentException("Season " + season + " does not exist");
            }
            Objects.requireNonNull(episode, "Episode cannot be null");

            List<Episode> episodes = seasons.get(season);
            if (episodes.stream()
                    .anyMatch(e -> e.getEpisodeNumber() == episode.getEpisodeNumber())) {
                throw new IllegalArgumentException("Episode already exists");
            }
            if (episode.getEpisodeNumber() != episodes.size() + 1) {
                throw new IllegalArgumentException("Invalid episode number");
            }
            seasons.get(season).add(episode);

            return this;
        }

        public TVSeriesBuilder addEpisodes(int season, List<Episode> episodes) {
            episodes.forEach(e -> addSingleEpisode(season, e));
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
