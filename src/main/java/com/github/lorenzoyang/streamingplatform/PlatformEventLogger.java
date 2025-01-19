package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.utils.PlatformEventVisitorAdapter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlatformEventLogger implements PlatformObserver {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final List<String> logMessages = new ArrayList<>();

    @Override
    public void notifyChange(PlatformEvent event) {
        event.accept(new PlatformEventVisitorAdapter() {
            @Override
            public void visitAddContent(AddContentEvent event) {
                String logMsg = formatContentMessage(
                        "Content Added",
                        event.getAddedContent()
                );
                log(logMsg);
            }

            @Override
            public void visitRemoveContent(RemoveContentEvent event) {
                String logMsg = formatContentMessage(
                        "Content Removed",
                        event.getRemovedContent()
                );
                log(logMsg);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                String logMsg = formatContentMessage(
                        "Content Updated",
                        event.getUpdatedContent()
                );
                log(logMsg);
            }
        });
    }

    private String formatContentMessage(String operation, Content content) {
        return String.format("%s: {Title='%s', Release Date='%s', %s}",
                operation,
                content.getTitle(),
                content.getReleaseDate().map(DATE_FORMATTER::format).orElse("Release date not specified"),
                content.isPremium() ? "premium" : "free"
        );
    }

    private void log(String message) {
        logMessages.add(message);
    }

    public Iterator<String> getLogMessagesIterator() {
        return logMessages.iterator();
    }

    public void clearLogs() {
        logMessages.clear();
    }
}
