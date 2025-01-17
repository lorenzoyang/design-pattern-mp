package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public abstract class Content {
    private final String title;
    private final boolean isFree;
    private final String description;
    private final LocalDate releaseDate;

    protected Content(ContentBuilder<?> builder) {
        this.title = builder.title;
        this.isFree = builder.isFree;
        this.description = builder.description;
        this.releaseDate = builder.releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFree() {
        return isFree;
    }

    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<LocalDate> getReleaseDate() {
        return Optional.ofNullable(releaseDate);
    }

    public abstract int getDurationInMinutes();

    public abstract <T> T accept(ContentVisitor<T> visitor);

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(getTitle(), content.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTitle());
    }

    protected abstract static class ContentBuilder<T extends ContentBuilder<T>> {
        private final String title;
        private boolean isFree;
        private String description;
        private LocalDate releaseDate;

        protected ContentBuilder(String title) {
            if (title == null || title.isBlank()) {
                throw new InvalidContentException("Content title cannot be null or blank");
            }
            this.title = title;
            this.isFree = true;
            this.description = null;
            this.releaseDate = null;
        }

        public T requiresSubscription() {
            this.isFree = false;
            return self();
        }

        public final T withDescription(String description) {
            this.description = Objects
                    .requireNonNull(description, "Content description cannot be null");
            if (this.description.isBlank()) {
                throw new InvalidContentException("Content description cannot be blank");
            }
            return self();
        }

        public final T withReleaseDate(LocalDate releaseDate) {
            this.releaseDate = Objects.requireNonNull(releaseDate, "Content release date cannot be null");
            return self();
        }

        protected abstract T self();

        public abstract Content build();
    }
}
