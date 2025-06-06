package com.github.lorenzoyang.freemediaplatform.events;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.utils.PlatformEventVisitor;

public class AddContentEvent implements PlatformEvent {
    private final Content addedContent;

    public AddContentEvent(Content addedContent) {
        this.addedContent = addedContent;
    }

    public Content getAddedContent() {
        return addedContent;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitAddContent(this);
    }
}
