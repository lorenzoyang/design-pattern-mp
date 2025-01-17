package com.github.lorenzoyang.streamingplatform.user;

public enum Gender {
    UNSPECIFIED("Unspecified"),
    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
