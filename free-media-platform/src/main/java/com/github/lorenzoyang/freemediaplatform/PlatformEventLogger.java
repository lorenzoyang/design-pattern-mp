package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.content.VideoResolution;
import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.PlatformEvent;
import com.github.lorenzoyang.freemediaplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.freemediaplatform.utils.PlatformEventVisitor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class PlatformEventLogger implements PlatformObserver {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final Collection<String> logMessages = new ArrayList<>();

    @Override
    public void notifyChange(PlatformEvent event) {
        event.accept(new PlatformEventVisitor() {
            @Override
            public void visitAddContent(AddContentEvent event) {
                String logMsg = "Content Added: " + formatContentMessage(event.getAddedContent());
                log(logMsg);
            }

            @Override
            public void visitRemoveContent(RemoveContentEvent event) {
                String logMsg = "Content Removed: " + formatContentMessage(event.getRemovedContent());
                log(logMsg);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                String logMsg = String.format("Content Updated: \n\tfrom: %s\n\tto: %s",
                        formatContentMessage(event.getOldContent()),
                        formatContentMessage(event.getUpdatedContent()));
                log(logMsg);
            }
        });
    }

    private String formatContentMessage(Content content) {
        return String.format("{Title='%s', Description='%s', Release Date='%s', Resolution='%s'}",
                content.getTitle(),
                content.getDescription().orElse("No description available"),
                content.getReleaseDate().map(DATE_FORMATTER::format).orElse("Release date not specified"),
                content.getResolution()
                        .map(VideoResolution::getDisplayName)
                        .orElse("Resolution not specified")
        );
    }

    private void log(String message) {
        logMessages.add(message);
    }

    public void clearLogs() {
        logMessages.clear();
    }

    public Iterator<String> logMessagesIterator() {
        return logMessages.iterator();
    }

    // package-private for testing purposes
    Collection<String> getLogMessages() {
        return this.logMessages;
    }
}
