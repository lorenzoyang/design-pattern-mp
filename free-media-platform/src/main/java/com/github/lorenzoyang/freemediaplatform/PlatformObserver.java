package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.events.PlatformEvent;

public interface PlatformObserver {
    void notifyChange(PlatformEvent event);
}
