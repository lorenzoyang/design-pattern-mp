package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.user.User;

import java.time.LocalDate;

public abstract class Content {
    private final String title;
    private final boolean isFree; // optional
    private final String description; // optional
    private final LocalDate releaseDate; // optional

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

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public abstract double getDurationMinutes();

    public final PlaybackResult play(User user, double progress, double elapsedTime) {
        ensureUserHasAccess(user);
        if (progress < 0 || progress > getDurationMinutes()) {
            throw new IllegalArgumentException("Progress must be between 0 and " + getDurationMinutes());
        }
        return playContent(progress, elapsedTime);
    }

    protected abstract PlaybackResult playContent(double progress, double elapsedTime);

    private void ensureUserHasAccess(User user) {
        if (!user.hasSubscription() && !isFree) {
            throw new IllegalArgumentException(
                    String.format("User %s does not have access to content '%s'.", user.getUsername(), getTitle())
            );
        }
    }

    protected abstract static class ContentBuilder<T extends ContentBuilder<T>> {
        private final String title;
        private boolean isFree; // optional
        private String description; // optional
        private LocalDate releaseDate; // optional

        protected ContentBuilder(String title) {
            if (title == null || title.isBlank()) {
                throw new IllegalArgumentException("Title cannot be null or blank");
            }
            this.title = title;
            // Default values
            this.isFree = true;
            this.description = "Description not provided";
            this.releaseDate = LocalDate.now();
        }

        public T requiresSubscription() {
            this.isFree = false;
            return self();
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
