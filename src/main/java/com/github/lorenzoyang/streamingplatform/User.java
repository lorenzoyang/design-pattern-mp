package com.github.lorenzoyang.streamingplatform;


import com.github.lorenzoyang.streamingplatform.contents.Content;
import com.github.lorenzoyang.streamingplatform.events.*;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidUserException;
import com.github.lorenzoyang.streamingplatform.utils.Gender;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


public class User implements PlatformObserver {
    private final String username;
    private final String password;
    private final String email;
    private final Integer age;
    private final Gender gender;
    private final boolean hasSubscription;

    private final Set<Content> toWatchList;
    private final Set<Content> watchedList;

    private User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.age = builder.age;
        this.gender = builder.gender;
        this.hasSubscription = builder.hasSubscription;

        this.toWatchList = new HashSet<>();
        this.watchedList = new HashSet<>();
    }

//    public void watch(Content content, int timeToWatch) {
//        Objects.requireNonNull(content, "Content cannot be null");
//        content.play(this, timeToWatch);
//        toWatchList.add(content);
//        watchedList.remove(content);
//    }

    @Override
    public void update(PlatformEvent event) {
        event.accept(new PlatformEventVisitor() {
            @Override
            public void visitAddContent(AddContentEvent event) {
                // do nothing
            }

            @Override
            public void visitRemoveContent(RemoveContentEvent event) {
                Content removedContent = event.getRemovedContent();
                toWatchList.remove(removedContent);
                watchedList.remove(removedContent);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                Content oldContent = event.getOldContent();
                Content updatedContent = event.getUpdatedContent();
                if (toWatchList.contains(oldContent)) {
                    toWatchList.remove(oldContent);
                    toWatchList.add(updatedContent);
                }
                if (watchedList.contains(oldContent)) {
                    watchedList.remove(oldContent);
                    watchedList.add(updatedContent);
                }
            }
        });
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

    // package-private getter for testing purposes
    Set<Content> getToWatchList() {
        return toWatchList;
    }

    // package-private getter for testing purposes
    Set<Content> getWatchedList() {
        return watchedList;
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
            if (username == null || username.contains(" ")) {
                throw new InvalidUserException("Username cannot be null or contain spaces");
            }
            if (password == null || password.contains(" ")) {
                throw new InvalidUserException("Password cannot be null or contain spaces");
            }
            this.username = username;
            this.password = password;
            this.email = "";
            this.age = 13;
            this.gender = Gender.UNSPECIFIED;
            this.hasSubscription = false;
        }

        public UserBuilder withEmail(String email) {
            if (email == null || !email.contains("@") || !email.contains(".")) {
                throw new InvalidUserException("Email must be a valid format containing '@' and '.'");
            }
            this.email = email;
            return this;
        }

        public UserBuilder withAge(int age) {
            if (age < 13 || age > 150) {
                throw new InvalidUserException("Age must be between 13 and 150.");
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
