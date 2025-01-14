package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.User;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

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

    public abstract int getDurationMinutes();

    public final String play(User user, int timeToWatch) {
        Objects.requireNonNull(user, "User cannot be null");

        if (timeToWatch < 0) {
            throw new IllegalArgumentException("Time to watch cannot be negative");
        }

        ensureUserHasAccess(user);

        return playContent(timeToWatch);
    }

    protected abstract String playContent(int timeToWatch);

    private void ensureUserHasAccess(User user) {
        if (!user.hasSubscription() && !isFree()) {
            throw new AccessDeniedException(
                    String.format("User %s does not have access to contents '%s'.", user.getUsername(), getTitle())
            );
        }
    }

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
                throw new InvalidContentException("Title cannot be null or blank");
            }
            this.title = title;
            this.isFree = true;
            this.description = "Description not provided";
            // to avoid to use LocalDate.now() as default value
            this.releaseDate = LocalDate.of(2000, 1, 1);
        }

        public T requiresSubscription() {
            this.isFree = false;
            return self();
        }

        public final T withDescription(String description) {
            Objects.requireNonNull(description, "Description cannot be null");
            if (description.isBlank()) {
                throw new InvalidContentException("Description cannot be blank");
            }
            this.description = description;
            return self();
        }

        public final T withReleaseDate(LocalDate releaseDate) {
            this.releaseDate = Objects.requireNonNull(releaseDate, "Release date cannot be null");
            return self();
        }

        protected abstract T self();

        public abstract Content build();
    }
}
