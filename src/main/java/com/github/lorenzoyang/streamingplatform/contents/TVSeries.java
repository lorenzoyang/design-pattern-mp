package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidSeasonException;
import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

import java.util.*;

public final class TVSeries extends Content {
    private final List<Season> seasons;
    private final int totalDurationMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);

        this.seasons = new ArrayList<>();
        builder.seasons.forEach(
                (seasonNumber, episodes) -> this.seasons.add(new Season(seasonNumber, episodes))
        );

        this.totalDurationMinutes = seasons.stream()
                .mapToInt(Season::getDurationMinutes)
                .sum();
    }

    public Iterator<Episode> getEpisodes(int seasonNumber) {
        if (seasonNumber < 1 || seasonNumber > seasons.size()) {
            throw new InvalidContentException("Season " + seasonNumber + " does not exist");
        }
        return seasons.get(seasonNumber - 1).getEpisodes();
    }

    public int getSeasonsCount() {
        return seasons.size();
    }

    @Override
    public int getDurationMinutes() {
        return totalDurationMinutes;
    }

    @Override
    protected String playContent(int timeToWatch) {
        var sb = new StringBuilder();
        sb.append("Playing TV series '").append(getTitle()).append("' for ").append(timeToWatch).append(" minutes.\n")
                .append("Watchable Episodes: ");
        for (Season season : seasons) {
            sb.append("Season ").append(season.getSeasonNumber()).append(": ");
            season.getEpisodes().forEachRemaining(e -> sb.append(e.getEpisodeNumber()).append(", "));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return visitor.visitTVSeries(this);
    }

    public List<Season> getSeasons() {
        return Collections.unmodifiableList(seasons);
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

        public TVSeriesBuilder addSeason(int seasonNumber) {
            if (seasonNumber != currentSeason + 1) {
                throw new InvalidSeasonException("Season number must be consecutive");
            }
            seasons.put(seasonNumber, new ArrayList<>());
            currentSeason = seasonNumber;
            return this;
        }

        public TVSeriesBuilder addSingleEpisode(int seasonNumber, Episode episode) {
            if (!seasons.containsKey(seasonNumber)) {
                throw new InvalidContentException("Season " + seasonNumber + " does not exist");
            }
            Objects.requireNonNull(episode, "Episode cannot be null");

            List<Episode> episodes = seasons.get(seasonNumber);
            if (episodes.stream()
                    .anyMatch(e -> e.getEpisodeNumber() == episode.getEpisodeNumber())) {
                throw new InvalidContentException("Episode already exists");
            }
            if (episode.getEpisodeNumber() != episodes.size() + 1) {
                throw new InvalidContentException("Episode number must be consecutive");
            }
            seasons.get(seasonNumber).add(episode);

            return this;
        }

        public TVSeriesBuilder addEpisodes(int seasonNumber, List<Episode> episodes) {
            episodes.forEach(e -> addSingleEpisode(seasonNumber, e));
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
