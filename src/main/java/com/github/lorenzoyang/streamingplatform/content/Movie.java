package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

import java.util.Objects;

public class Movie extends Content {
    private final Episode episode;

    private Movie(MovieBuilder builder) {
        super(builder);
        this.episode = builder.episode;
    }

    public Episode getEpisode() {
        return episode;
    }

    @Override
    public int getDurationMinutes() {
        return episode.getDurationMinutes();
    }

    @Override
    protected ViewingProgress playContent(ViewingProgress currentProgress, int timeToWatch) {
        int totalViewingDuration = Math.min(
                currentProgress.getTotalViewingDuration() + timeToWatch,
                this.getDurationMinutes()
        );

        return ViewingProgress.of(
                this.getEpisode(),
                totalViewingDuration - currentProgress.getTotalViewingDuration(),
                totalViewingDuration
        );
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return visitor.visitMovie(this);
    }

    public static class MovieBuilder extends ContentBuilder<MovieBuilder> {
        private final Episode episode;

        public MovieBuilder(String title, Episode episode) {
            super(title);
            Objects.requireNonNull(episode, "Episode cannot be null");
            if (episode.getEpisodeNumber() != 1) {
                throw new IllegalArgumentException("Movie can only have one episode");
            }
            this.episode = episode;
        }

        @Override
        protected MovieBuilder self() {
            return this;
        }

        @Override
        public Movie build() {
            return new Movie(this);
        }
    }
}
