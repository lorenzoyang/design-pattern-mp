package com.github.lorenzoyang.freemediaplatform.utils;

public interface EmailNotificationService {
    void notifyUser(String email, String subject, String message);
}
