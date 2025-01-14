package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.contents.Content;

public class AddContentEvent implements PlatformEvent {
    private final Content addedContent;

    public AddContentEvent(Content content) {
        this.addedContent = content;
    }

    public Content getAddedContent() {
        return addedContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitAddContent(this);
    }
}
