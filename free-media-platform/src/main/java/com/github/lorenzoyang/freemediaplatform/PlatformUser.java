package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.PlatformEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.freemediaplatform.utils.EmailNotificationService;
import com.github.lorenzoyang.freemediaplatform.utils.PlatformEventVisitorAdapter;

import java.util.Objects;

public class PlatformUser implements PlatformObserver {
    private final String email;
    private final EmailNotificationService emailNotificationService;

    public PlatformUser(String email, EmailNotificationService emailNotificationService) {
        Objects.requireNonNull(email, "Email cannot be null");
        this.email = email;
        Objects.requireNonNull(emailNotificationService, "EmailNotificationService cannot be null");
        this.emailNotificationService = emailNotificationService;
    }

    @Override
    public void notifyChange(PlatformEvent event) {
        event.accept(new PlatformEventVisitorAdapter() {
            @Override
            public void visitAddContent(AddContentEvent event) {
                String notificationMsg = "New content added: " + event.getAddedContent().getTitle() +
                        ". Check your email for details.";
                emailNotificationService.notifyUser(email, "New Content Added", notificationMsg);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                String notificationMsg = "Content updated: " + event.getUpdatedContent().getTitle() +
                        ". Check your email for details.";
                emailNotificationService.notifyUser(email, "Content Updated", notificationMsg);
            }
        });
    }
}
