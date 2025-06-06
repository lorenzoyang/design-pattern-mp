package com.github.lorenzoyang.freemediaplatform.events;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.utils.PlatformEventVisitor;

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
