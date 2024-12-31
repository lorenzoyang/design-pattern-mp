package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

import java.util.Objects;

import static java.lang.System.lineSeparator;

public class Movie extends Content {
    private final Video video;

    private Movie(MovieBuilder builder) {
        super(builder);
        this.video = builder.video;
    }

    public Video getVideo() {
        return video;
    }

    @Override
    public String getDetailedContentInfo() {
        return "\tFilm Resolution: " + getVideo().getResolution() + lineSeparator() +
                "\tFilm Duration: " + getVideo().getDurationMinutes() + " minutes" + lineSeparator() +
                "\tFilm Video Info: " + getVideo().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(getVideo(), movie.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getVideo());
    }

    public static class MovieBuilder extends ContentBuilder<MovieBuilder> {
        private final Video video;

        public MovieBuilder(String title, Video video) {
            super(title);
            this.video = Objects.requireNonNull(video, "Video cannot be null");
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
