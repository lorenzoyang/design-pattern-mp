package com.github.lorenzoyang.streamingplatform.user;


import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.PlaybackResult;

import java.util.*;

import static com.github.lorenzoyang.streamingplatform.user.UserValidations.*;


public class User {
    private final String username;
    private final String password;
    private final String email;
    private final Integer age;
    private final Gender gender;
    private final boolean hasSubscription;

    private final Map<Content, Double> toWatchList;
    private final List<Content> watchedList;

    private User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.age = builder.age;
        this.gender = builder.gender;
        this.hasSubscription = builder.hasSubscription;

        this.toWatchList = new HashMap<>();
        this.watchedList = new ArrayList<>();
    }

    public void watch(Content content, double elapsedTime) {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        double progress = toWatchList.getOrDefault(content, 0.0);
        PlaybackResult playbackResult = content.play(this, progress, elapsedTime);
        if (playbackResult.getProgress() == content.getDurationMinutes()) {
            toWatchList.remove(content);
            watchedList.add(content);
        } else {
            toWatchList.put(content, playbackResult.getProgress());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean hasSubscription() {
        return hasSubscription;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }

    public static class UserBuilder {
        private final String username;
        private final String password;
        private String email;
        private Integer age;
        private Gender gender;
        private boolean hasSubscription;

        public UserBuilder(String username, String password) {
            if (username == null || username.length() < MIN_USERNAME_LENGTH || username.length() > MAX_USERNAME_LENGTH) {
                throw new IllegalArgumentException(INVALID_USERNAME_MESSAGE);
            }
            if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
                throw new IllegalArgumentException(INVALID_PASSWORD_MESSAGE);
            }
            this.username = username;
            this.password = password;
            this.email = null;
            this.age = null;
            this.gender = Gender.UNSPECIFIED;
            this.hasSubscription = false;
        }

        public UserBuilder withEmail(String email) {
            if (email == null || !email.contains("@") || !email.contains(".")) {
                throw new IllegalArgumentException(INVALID_EMAIL_MESSAGE);
            }
            this.email = email;
            return this;
        }

        public UserBuilder withAge(int age) {
            if (age < MIN_AGE || age > MAX_AGE) {
                throw new IllegalArgumentException(INVALID_AGE_MESSAGE);
            }
            this.age = age;
            return this;
        }

        public UserBuilder withGender(Gender gender) {
            this.gender = Objects.requireNonNull(gender, "Gender cannot be null");
            return this;
        }

        public UserBuilder subscribe() {
            this.hasSubscription = true;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
