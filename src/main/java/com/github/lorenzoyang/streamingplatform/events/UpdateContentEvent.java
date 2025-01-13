package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.content.Content;

public class UpdateContentEvent implements PlatformEvent {
    private final Content oldContent;
    private final Content updatedContent;

    public UpdateContentEvent(Content oldContent, Content updatedContent) {
        this.oldContent = oldContent;
        this.updatedContent = updatedContent;
    }

    public Content getOldContent() {
        return oldContent;
    }

    public Content getUpdatedContent() {
        return updatedContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitUpdateContent(this);
    }
}
