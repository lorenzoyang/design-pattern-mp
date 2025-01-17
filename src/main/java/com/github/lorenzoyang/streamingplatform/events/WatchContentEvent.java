package com.github.lorenzoyang.streamingplatform.events;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.user.User;

public class WatchContentEvent implements PlatformEvent {
    private final User user;
    private final Content content;
    private final int timeToWatch;

    public WatchContentEvent(User user, Content content, int timeToWatch) {
        this.user = user;
        this.content = content;
        this.timeToWatch = timeToWatch;
    }

    public User getUser() {
        return user;
    }

    public Content getContent() {
        return content;
    }

    public int getTimeToWatch() {
        return timeToWatch;
    }

    @Override
    public void accept(PlatformEventVisitor visitor) {
        visitor.visitWatchContent(this);
    }
}
