package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.user.User;

import java.time.LocalDate;
import java.util.Objects;

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

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public abstract double getDurationMinutes();

    public final ViewingProgress play(User user, ViewingProgress currentProgress, double timeToWatch) {
        Objects.requireNonNull(user, "User cannot be null");
        Objects.requireNonNull(currentProgress, "Current progress cannot be null");
        if (timeToWatch < 0) {
            throw new IllegalArgumentException("Time to watch cannot be negative");
        }

        ensureUserHasAccess(user);

        if (currentProgress.isCompleted(this)) {
            throw new IllegalArgumentException("Content has already been completed");
        }

        return playContent(currentProgress, timeToWatch);
    }

    protected abstract ViewingProgress playContent(ViewingProgress currentProgress, double timeToWatch);

    private void ensureUserHasAccess(User user) {
        if (!user.hasSubscription() && !isFree()) {
            throw new AccessDeniedException(
                    String.format("User %s does not have access to content '%s'.", user.getUsername(), getTitle())
            );
        }
    }

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
                throw new InvalidContentException("Title cannot be null or blank");
            }
            this.title = title;
            this.isFree = true;
            this.description = "Description not provided";
            this.releaseDate = LocalDate.now();
        }

        public T requiresSubscription() {
            this.isFree = false;
            return self();
        }

        public final T withDescription(String description) {
            if (description == null || description.isBlank()) {
                throw new InvalidContentException("Description cannot be null or blank");
            }
            this.description = description;
            return self();
        }

        public final T withReleaseDate(LocalDate releaseDate) {
            if (releaseDate == null) {
                throw new InvalidContentException("Release date cannot be null");
            }
            if (releaseDate.isAfter(LocalDate.now())) {
                throw new InvalidContentException("Release date cannot be in the future");
            }
            this.releaseDate = releaseDate;
            return self();
        }

        protected abstract T self();

        public abstract Content build();
    }
}
