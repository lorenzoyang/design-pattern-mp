package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.content.Content;

public class ReplaceContentEvent implements PlatformEvent {
    private final Content oldContent;
    private final Content newContent;

    public ReplaceContentEvent(Content oldContent, Content newContent) {
        this.oldContent = oldContent;
        this.newContent = newContent;
    }

    public Content getOldContent() {
        return oldContent;
    }

    public Content getNewContent() {
        return newContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitReplaceContent(this);
    }
}
