package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;

public interface PlatformObserver {
    void update(PlatformEvent event);
}
