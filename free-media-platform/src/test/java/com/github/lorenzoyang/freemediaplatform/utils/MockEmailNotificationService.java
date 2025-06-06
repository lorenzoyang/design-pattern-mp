package com.github.lorenzoyang.freemediaplatform.utils;

public class MockEmailNotificationService implements EmailNotificationService {
    private String lastNotificationMessage = "";

    public String getLastNotificationMessage() {
        return lastNotificationMessage;
    }

    @Override
    public void notifyUser(String email, String subject, String message) {
        lastNotificationMessage = "To: " + email + "\n" +
                "Subject: " + subject + "\n" +
                "Message: " + message;
    }
}
