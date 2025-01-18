package com.github.lorenzoyang.streamingplatform.user;

import com.github.lorenzoyang.streamingplatform.PlatformObserver;
import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.events.WatchContentEvent;
import com.github.lorenzoyang.streamingplatform.utils.PlatformEventVisitorAdapter;

import java.util.*;

public class UserWatchlistManager implements PlatformObserver {
    private final Map<User, Set<Content>> inProgressContents = new HashMap<>();
    private final Map<User, Set<Content>> completedContents = new HashMap<>();

    public Iterator<Content> getInProgressContentsIterator(User user) {
        return inProgressContents.getOrDefault(user, Collections.emptySet()).iterator();
    }

    public Iterator<Content> getCompletedContentsIterator(User user) {
        return completedContents.getOrDefault(user, Collections.emptySet()).iterator();
    }

    private void addToInProgress(User user, Content content) {
        if (isNotCompleted(user, content)) {
            inProgressContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
        }
    }

    private void markAsCompleted(User user, Content content) {
        inProgressContents.computeIfAbsent(user, u -> new HashSet<>()).remove(content);
        completedContents.computeIfAbsent(user, u -> new HashSet<>()).add(content);
    }

    private boolean isNotCompleted(User user, Content content) {
        return !completedContents.containsKey(user) ||
                !completedContents.get(user).contains(content);
    }

    private void updateContentInMap(Map<User, Set<Content>> map, Content oldContent, Content newContent) {
        map.values().forEach(contents -> {
            if (contents.contains(oldContent)) {
                contents.remove(oldContent);
                contents.add(newContent);
            }
        });
    }

    private void removeContentFromMap(Map<User, Set<Content>> map, Content content) {
        map.values().forEach(contents -> contents.remove(content));
    }

    @Override
    public void notifyChange(PlatformEvent event) {
        event.accept(new PlatformEventVisitorAdapter() {
            @Override
            public void visitRemoveContent(RemoveContentEvent event) {
                Content removedContent = event.getRemovedContent();
                removeContentFromMap(inProgressContents, removedContent);
                removeContentFromMap(completedContents, removedContent);
            }

            @Override
            public void visitUpdateContent(UpdateContentEvent event) {
                Content oldContent = event.getOldContent();
                Content updatedContent = event.getUpdatedContent();
                updateContentInMap(inProgressContents, oldContent, updatedContent);
                updateContentInMap(completedContents, oldContent, updatedContent);
            }

            @Override
            public void visitWatchContent(WatchContentEvent event) {
                User user = event.getUser();
                Content content = event.getContent();
                if (event.getTimeToWatch() >= content.getDurationInMinutes()) {
                    markAsCompleted(user, content);
                } else if (isNotCompleted(user, content)) {
                    addToInProgress(user, content);
                }
            }
        });
    }

    // package-private for testing purposes
    Map<User, Set<Content>> getInProgressContents() {
        return inProgressContents;
    }

    // package-private for testing purposes
    Map<User, Set<Content>> getCompletedContents() {
        return completedContents;
    }
}
