package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.utils.PlatformEventVisitor;

public interface PlatformEvent {
    void accept(PlatformEventVisitor visitor);
}
