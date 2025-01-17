package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidSeasonException;
import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

import java.util.*;

public class TVSeries extends Content {
    private final List<Season> seasons;
    private final int durationInMinutes;

    private TVSeries(TVSeriesBuilder builder) {
        super(builder);
        this.seasons = new ArrayList<>();
        builder.seasons.forEach(
                (seasonNumber, episodes) -> this.seasons.add(new Season(seasonNumber, episodes))
        );
        this.durationInMinutes = seasons.stream()
                .mapToInt(Season::getDurationInMinutes)
                .sum();
    }

    public int getSeasonsCount() {
        return seasons.size();
    }

    public Iterator<Episode> getEpisodes(int seasonNumber) {
        if (seasonNumber < 1 || seasonNumber > seasons.size()) {
            throw new InvalidContentException("Season " + seasonNumber + " does not exist");
        }
        return seasons.get(seasonNumber - 1).getEpisodes();
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
