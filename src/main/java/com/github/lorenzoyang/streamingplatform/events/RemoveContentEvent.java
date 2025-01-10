package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.content.Content;

public class RemoveContentEvent implements PlatformEvent {
    private final Content removedContent;

    public RemoveContentEvent(Content content) {
        this.removedContent = content;
    }

    public Content getRemovedContent() {
        return removedContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitRemoveContent(this);
    }
}
