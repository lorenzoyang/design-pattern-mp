package com.github.lorenzoyang.streamingplatform.user;

final class UserValidations {
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 30;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 30;
    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 150;

    public static final String INVALID_USERNAME_MESSAGE =
            "Username must be between " + MIN_USERNAME_LENGTH + " and " + MAX_USERNAME_LENGTH + " characters.";
    public static final String INVALID_PASSWORD_MESSAGE =
            "Password must be between " + MIN_PASSWORD_LENGTH + " and " + MAX_PASSWORD_LENGTH + " characters.";
    public static final String INVALID_EMAIL_MESSAGE =
            "Email must be a valid format containing '@' and '.'";
    public static final String INVALID_AGE_MESSAGE =
            "Age must be between " + MIN_AGE + " and " + MAX_AGE + ".";
}
