package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.*;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.ContentProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class StreamingPlatform {
    private final String name;
    private final List<Content> contents;
    private final List<User> users;
    private final Collection<PlatformObserver> observers;

    public StreamingPlatform(String name, ContentProvider<Content> contentProvider) {
        this.name = Objects.requireNonNull(name, "Streaming platform name cannot be null");

        Objects.requireNonNull(contentProvider, "Content provider cannot be null");
        this.contents = contentProvider.retrieve()
                .collect(Collectors.toList());

        this.users = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void registerUser(User user) {
        if (users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is already registered");
        }
        users.add(user);
    }

    public void unregisterUser(User user) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is not registered");
        }
        users.remove(user);
    }

    public void addContent(Content newContent) {
        if (contents.contains(newContent)) {
            throw new IllegalArgumentException("Content '" + newContent.getTitle() + "' already exists");
        }
        contents.add(newContent);
        notifyObservers(new AddContentEvent(newContent));
    }

    public void removeContent(Content existingContent) {
        if (!contents.contains(existingContent)) {
            throw new IllegalArgumentException("Content '" + existingContent.getTitle() + "' does not exist");
        }
        contents.remove(existingContent);
        notifyObservers(new RemoveContentEvent(existingContent));
    }

    public void updateContent(Content updatedContent) {
        Content oldContent = contents.stream()
                .filter(existingContent -> existingContent.equals(updatedContent))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Content '" + updatedContent.getTitle() + "' does not exist")
                );

        contents.remove(oldContent);
        contents.add(updatedContent);
        notifyObservers(new UpdateContentEvent(oldContent, updatedContent));
    }

    public String watchContent(User user, Content content, int timeToWatch) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is not registered");
        }
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        notifyObservers(new WatchContentEvent(user, content, timeToWatch));

        return "User '" + user.getUsername() + "' watched '" + content.getTitle() + "' for " + timeToWatch + " minutes";
    }

    public void addObserver(PlatformObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PlatformObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(PlatformEvent event) {
        observers.forEach(observer -> observer.notifyChange(event));
    }

    public String getName() {
        return name;
    }
}
