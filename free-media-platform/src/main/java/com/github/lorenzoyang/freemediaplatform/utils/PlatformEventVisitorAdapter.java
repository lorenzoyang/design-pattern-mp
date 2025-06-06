package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;

public abstract class PlatformEventVisitorAdapter implements PlatformEventVisitor {
    @Override
    public void visitAddContent(AddContentEvent event) {
        // Do nothing
    }

    @Override
    public void visitRemoveContent(RemoveContentEvent event) {
        // Do nothing
    }

    @Override
    public void visitUpdateContent(UpdateContentEvent event) {
        // Do nothing
    }
}
