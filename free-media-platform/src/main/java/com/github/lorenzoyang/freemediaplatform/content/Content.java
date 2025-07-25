package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.freemediaplatform.utils.ContentVisitor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public abstract class Content {
    private final String title;
    private final String description;
    private final LocalDate releaseDate;
    private final VideoResolution resolution;

    protected Content(ContentBuilder<?> builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.releaseDate = builder.releaseDate;
        this.resolution = builder.resolution;
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<LocalDate> getReleaseDate() {
        return Optional.ofNullable(releaseDate);
    }

    public Optional<VideoResolution> getResolution() {
        return Optional.ofNullable(resolution);
    }

    public abstract int getDurationInMinutes();

    public abstract <T> T accept(ContentVisitor<T> visitor);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Content)) return false;
        Content other = (Content) obj;
        return Objects.equals(getTitle(), other.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTitle());
    }

    protected abstract static class ContentBuilder<T extends ContentBuilder<T>> {
        private final String title;
        private String description = null;
        private LocalDate releaseDate = null;
        private VideoResolution resolution = null;

        protected ContentBuilder(String title) {
            Objects.requireNonNull(title, "Content title cannot be null");
            if (title.isBlank()) {
                throw new InvalidContentException("Content title cannot be blank");
            }
            this.title = title;
        }

        public T withDescription(String description) {
            if (description != null && description.isBlank()) {
                throw new InvalidContentException("Content description cannot be blank");
            }
            this.description = description;
            return self();
        }

        public T withReleaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return self();
        }

        public T withResolution(VideoResolution resolution) {
            this.resolution = resolution;
            return self();
        }

        protected abstract T self();

        public abstract Content build();
    }
}
