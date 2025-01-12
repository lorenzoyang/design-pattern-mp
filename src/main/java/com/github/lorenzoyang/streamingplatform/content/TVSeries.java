package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.utils.Episode;
import com.github.lorenzoyang.streamingplatform.content.utils.Season;
import com.github.lorenzoyang.streamingplatform.content.utils.ViewingProgress;

import java.util.*;

public final class TVSeries extends Content {
    private final List<Season> seasons;

    private final double totalDurationMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.seasons = new ArrayList<>();

        builder.seasons.forEach((seasonNumber, episodes) -> {
            this.seasons.add(new Season(seasonNumber, episodes));
        });

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
        double totalViewingDuration = currentProgress.getTotalViewingDuration();
        Episode startingEpisode = null;
        for (Season season : seasons) {
            Iterator<Episode> episodes = season.getEpisodes();
            while (episodes.hasNext()) {
                startingEpisode = episodes.next();
                if (totalViewingDuration < startingEpisode.getDurationMinutes()) {
                    break;
                }
                totalViewingDuration -= startingEpisode.getDurationMinutes();
            }
        }

        if (startingEpisode == null) {
            return ViewingProgress.empty();
        }

        totalViewingDuration = Math.min(totalViewingDuration + timeToWatch, this.getDurationMinutes());

        return ViewingProgress.of(
                startingEpisode,
                totalViewingDuration - currentProgress.getTotalViewingDuration(),
                totalViewingDuration
        );
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

        public TVSeriesBuilder addEpisode(int season, Episode episode) {
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
            episodes.forEach(e -> addEpisode(season, e));
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
