package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.utils.PlatformEventVisitor;

public class RemoveContentEvent implements PlatformEvent {
    private final Content removedContent;

    public RemoveContentEvent(Content removedContent) {
        this.removedContent = removedContent;
    }

    public Content getRemovedContent() {
        return removedContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitRemoveContent(this);
    }
}
