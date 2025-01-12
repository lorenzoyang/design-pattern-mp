package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

public class MockObserver implements PlatformObserver {
    private PlatformEvent event;

    @Override
    public void update(PlatformEvent event) {
        this.event = event;
    }

    public PlatformEvent getEvent() {
        return event;
    }
}
