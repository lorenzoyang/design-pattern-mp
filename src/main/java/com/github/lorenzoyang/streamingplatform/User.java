package com.github.lorenzoyang.streamingplatform;


import com.github.lorenzoyang.streamingplatform.exceptions.InvalidUserException;
import com.github.lorenzoyang.streamingplatform.utils.Gender;

import java.util.Objects;
import java.util.Optional;


public class User {
    private final String username;
    private final String password;
    private final String email;
    private final Integer age;
    private final Gender gender;
    private final boolean hasSubscription;

    private User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
        this.age = builder.age;
        this.gender = builder.gender;
        this.hasSubscription = builder.hasSubscription;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<Integer> getAge() {
        return Optional.ofNullable(age);
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
            if (username == null || username.contains(" ")) {
                throw new InvalidUserException("Username cannot be null or contain spaces");
            }
            if (password == null || password.contains(" ")) {
                throw new InvalidUserException("Password cannot be null or contain spaces");
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
