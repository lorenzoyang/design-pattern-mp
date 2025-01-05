package com.github.lorenzoyang.streamingplatform.exceptions;

public class InvalidContentException extends RuntimeException {
    public InvalidContentException(String message) {
        super(message);
    }
}