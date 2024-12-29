package com.github.lorenzoyang.streamingplatform;

import java.util.Objects;

public class User {
    private final String username;
    private final String password;

    private String email;
    private int age;
    private Gender gender;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
        this.age = 0; // 0 means unspecified
        this.gender = Gender.UNSPECIFIED;
    }

    public User email(String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        this.email = email;
        return this;
    }

    public User age(int age) {
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be greater than 0");
        }
        this.age = age;
        return this;
    }

    public User gender(Gender gender) {
        this.gender = gender;
        return this;
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
}
