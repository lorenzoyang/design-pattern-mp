package com.github.lorenzoyang.streamingplatform.user;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserWatchlistManager implements PlatformObserver {
    private final Map<User, Set<Content>> inProgressContents;
    private final Map<User, Set<Content>> completedContents;

    public UserWatchlistManager() {
        this.inProgressContents = new HashMap<>();
        this.completedContents = new HashMap<>();
    }

    public void addToInProgress(User user, Content content) {
        inProgressContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
    }

    public void markAsCompleted(User user, Content content) {
        inProgressContents.computeIfAbsent(user, u -> new HashSet<>()).remove(content);
        completedContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
    }

    @Override
    public void notifyChange(PlatformEvent event) {

    }
}
