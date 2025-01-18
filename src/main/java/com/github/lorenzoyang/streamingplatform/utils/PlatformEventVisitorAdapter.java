package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.events.WatchContentEvent;

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

    @Override
    public void visitWatchContent(WatchContentEvent event) {
        // Do nothing
    }
}
