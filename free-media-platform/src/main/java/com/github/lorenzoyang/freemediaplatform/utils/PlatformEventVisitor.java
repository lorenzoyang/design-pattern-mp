package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;

public interface PlatformEventVisitor {
    void visitAddContent(AddContentEvent event);

    void visitRemoveContent(RemoveContentEvent event);

    void visitUpdateContent(UpdateContentEvent event);
}
