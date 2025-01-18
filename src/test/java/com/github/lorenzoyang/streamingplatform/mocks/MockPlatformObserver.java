package com.github.lorenzoyang.streamingplatform.mocks;

import com.github.lorenzoyang.streamingplatform.PlatformObserver;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;

public class MockPlatformObserver implements PlatformObserver {
    private PlatformEvent event;

    @Override
    public void notifyChange(PlatformEvent event) {
        this.event = event;
    }

    public PlatformEvent getEvent() {
        return event;
    }
}
