package com.github.lorenzoyang.streamingplatform.events;

public interface PlatformEvent {
    void accept(PlatformEventVisitor visitor);
}
