package com.github.lorenzoyang.streamingplatform.events;

public interface PlatformEventVisitor {
    void visitAddContent(AddContentEvent event);

    void visitRemoveContent(RemoveContentEvent event);

    void visitUpdateContent(UpdateContentEvent event);
}
