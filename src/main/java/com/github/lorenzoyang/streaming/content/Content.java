package com.github.lorenzoyang.streaming.content;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Content {
    private final String title;
    private final String description; // optional
    private final LocalDate releaseDate; // optional

    protected Content(String title, String description, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    protected abstract static class Builder<T extends Builder<T>> {
        protected String title;
        protected String description; // optional
        protected LocalDate releaseDate; // optional

        public Builder(String title) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Title cannot be null or blank");
            }
            this.title = title;
            this.description = "";
            this.releaseDate = LocalDate.now();
        }

        public T withDescription(String description) {
            this.description = Objects.requireNonNull(description, "Description cannot be null");
            return self();
        }

        public T withReleaseDate(LocalDate releaseDate) {
            if (releaseDate == null) {
                throw new IllegalArgumentException("Release date cannot be null");
            }
            if (releaseDate.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Release date cannot be in the future");
            }
            this.releaseDate = releaseDate;
            return self();
        }

        protected abstract T self();

        public abstract Content build();
    }
}
