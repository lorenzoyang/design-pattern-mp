package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;

import java.util.Objects;

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
    public double getDurationMinutes() {
        return video.getDurationMinutes();
    }

    @Override
    protected ViewingProgress playContent(ViewingProgress currentProgress, double timeToWatch) {
        double totalViewingDuration = Math.min(
                currentProgress.getTotalViewingDuration() + timeToWatch,
                getDurationMinutes()
        );
        
        return ViewingProgress.of(
                getVideo(),
                totalViewingDuration - currentProgress.getTotalViewingDuration(),
                totalViewingDuration
        );
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
