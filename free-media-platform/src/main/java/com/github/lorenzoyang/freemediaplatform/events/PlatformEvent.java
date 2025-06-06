package com.github.lorenzoyang.freemediaplatform.events;

import com.github.lorenzoyang.freemediaplatform.utils.PlatformEventVisitor;

public interface PlatformEvent {
    void accept(PlatformEventVisitor visitor);
}
