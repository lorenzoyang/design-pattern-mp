package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.events.WatchContentEvent;

public interface PlatformEventVisitor {
    void visitAddContent(AddContentEvent event);

    void visitRemoveContent(RemoveContentEvent event);

    void visitUpdateContent(UpdateContentEvent event);

    void visitWatchContent(WatchContentEvent event);
}
