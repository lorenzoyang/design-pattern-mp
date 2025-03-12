package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.freemediaplatform.utils.ContentVisitor;

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
    public int getDurationInMinutes() {
        return episode.getDurationInMinutes();
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return visitor.visitMovie(this);
    }

    public static class MovieBuilder extends ContentBuilder<MovieBuilder> {
        private final Episode episode;

        public MovieBuilder(String title, Episode episode) {
            super(title);
            this.episode = Objects.requireNonNull(episode, "Episode cannot be null");
            if (this.episode.getEpisodeNumber() != 1) {
                throw new InvalidContentException("A movie can only have one episode with episode number 1");
            }
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
