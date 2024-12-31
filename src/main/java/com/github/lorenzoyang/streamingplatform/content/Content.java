package com.github.lorenzoyang.streamingplatform.content;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.lang.System.lineSeparator;

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

    public String getContentDetails() {
        var europeanFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Title: " + title + lineSeparator() +
                "Description: " + description + lineSeparator() +
                ">>>" + lineSeparator() +
                getDetailedContentInfo() + lineSeparator() +
                "<<<" + lineSeparator() +
                "Release date: " + releaseDate.format(europeanFormatter) + lineSeparator();
    }

    protected abstract String getDetailedContentInfo();

    protected abstract static class ContentBuilder<T extends ContentBuilder<T>> {
        private final String title;
        private String description; // optional
        private LocalDate releaseDate; // optional

        protected ContentBuilder(String title) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Title cannot be null or blank");
            }
            this.title = title;
            // Default values
            this.description = "Description not provided";
            this.releaseDate = LocalDate.now();
        }

        public T withDescription(String description) {
            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("Description cannot be null or blank");
            }
            this.description = description;
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
