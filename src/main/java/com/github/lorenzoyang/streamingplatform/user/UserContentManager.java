package com.github.lorenzoyang.streamingplatform.user;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserContentManager implements PlatformObserver {
    private final Map<User, Set<Content>> userToWatchContents;
    private final Map<User, Set<Content>> userWatchedContents;

    public UserContentManager() {
        this.userToWatchContents = new HashMap<>();
        this.userWatchedContents = new HashMap<>();
    }

    public void addToWatchContent(User user, Content content) {
        userToWatchContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
    }

    public void markAsWatchedContent(User user, Content content) {
        userToWatchContents.computeIfAbsent(user, u -> new HashSet<>()).remove(content);
        userWatchedContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
    }

    @Override
    public void notifyChange(PlatformEvent event) {

    }
}
