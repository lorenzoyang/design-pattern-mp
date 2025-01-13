package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.*;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlatformEventLogger implements PlatformObserver {
    private final List<String> logMessages = new ArrayList<>();

    @Override
    public void update(PlatformEvent event) {
        event.accept(new PlatformEventVisitor() {
            @Override
            public void visitAddContent(AddContentEvent event) {
                String logMessage = formatContentMessage(
                        "Content Added",
                        event.getAddedContent()
                );
                log(logMessage);
            }

            @Override
            public void visitRemoveContent(RemoveContentEvent event) {
                String logMessage = formatContentMessage(
                        "Content Removed",
                        event.getRemovedContent()
                );
                log(logMessage);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                String logMessage = formatContentMessage(
                        "Content Updated",
                        event.getUpdatedContent()
                );
                log(logMessage);
            }
        });
    }

    private String formatContentMessage(String operation, Content content) {
        return String.format("%s: {name='%s', description='%s', %s}",
                operation,
                content.getTitle(),
                content.getDescription(),
                content.isFree() ? "free" : "premium"
        );
    }

    private void log(String message) {
        logMessages.add(message);
    }

    public List<String> getLogMessages() {
        return Collections.unmodifiableList(logMessages);
    }

    public void clearLogs() {
        logMessages.clear();
    }
}
