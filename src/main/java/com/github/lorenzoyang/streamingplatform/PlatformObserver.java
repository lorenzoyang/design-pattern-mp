package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;

public interface PlatformObserver {
    void notifyChange(PlatformEvent event);
}
