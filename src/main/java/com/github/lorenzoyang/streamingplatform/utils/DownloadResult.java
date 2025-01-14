package com.github.lorenzoyang.streamingplatform.utils;

public class DownloadResult {
    private final boolean success;
    private final String message;

    public DownloadResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}