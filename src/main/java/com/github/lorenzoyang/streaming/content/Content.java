package com.github.lorenzoyang.streaming.content;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Content {
    private final String title;
    private final String description; // optional
    private final LocalDate releaseDate; // optional

    protected Content(ContentBuilder<?> builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.releaseDate = builder.releaseDate;
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

    protected abstract static class ContentBuilder<T extends ContentBuilder<T>> {
        private final String title;
        private String description; // optional
        private LocalDate releaseDate; // optional

        protected ContentBuilder(String title) {
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
