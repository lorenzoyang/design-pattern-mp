package com.github.lorenzoyang.streaming.content.model;

import com.github.lorenzoyang.streaming.content.Content;
import com.github.lorenzoyang.streaming.content.Video;

import java.time.LocalDate;

public class Movie extends Content {
    private final Video video;

    private Movie(String title, String description, LocalDate releaseDate, Video video) {
        super(title, description, releaseDate);
        this.video = video;
    }

    public Video getVideo() {
        return video;
    }

    public static class MovieBuilder extends Content.Builder<MovieBuilder> {
        private final Video video;

        public MovieBuilder(String title, Video video) {
            super(title);
            this.video = video;
        }

        @Override
        protected MovieBuilder self() {
            return this;
        }

        @Override
        public Movie build() {
            return new Movie(title, description, releaseDate, video);
        }
    }
}
