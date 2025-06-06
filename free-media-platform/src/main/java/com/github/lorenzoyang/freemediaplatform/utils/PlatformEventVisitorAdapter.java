package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;

public abstract class PlatformEventVisitorAdapter implements PlatformEventVisitor {
    @Override
    public void visitAddContent(AddContentEvent event) {
        // default implementation
    }

    @Override
    public void visitRemoveContent(RemoveContentEvent event) {
        // default implementation
    }

    @Override
    public void visitUpdateContent(UpdateContentEvent event) {
        // default implementation
    }
}
