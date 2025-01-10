package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.ReplaceContentEvent;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.*;

public final class StreamPlatform {
    private final String name;

    private final List<Content> contents;
    private final List<User> users;

    private final Collection<PlatformObserver> observers;

    public StreamPlatform(String name, DataProvider<Content> contentProvider, DataProvider<User> userProvider) {
        this.name = name;
        this.contents = new ArrayList<>(contentProvider.fetchData());
        this.users = new ArrayList<>(userProvider.fetchData());
        this.observers = new ArrayList<>(users);
    }

    /**
     * Returns an iterator over the observers of the platform.
     * Testing purposes only.
     *
     * @return an iterator over the observers of the platform
     */
    Collection<PlatformObserver> getObservers() {
        return observers;
    }

    public void attach(PlatformObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(PlatformObserver observer) {
        observers.remove(observer);
    }

    public String getName() {
        return name;
    }

    public Iterator<Content> getContents() {
        return contents.iterator();
    }

    public Iterator<User> getUsers() {
        return users.iterator();
    }

    public void registerUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (users.contains(user)) {
            throw new IllegalArgumentException("User already exists");
        }
        users.add(user);
        observers.add(user);
    }

    public void unregisterUser(User user) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User does not exist");
        }
        users.remove(user);
        observers.remove(user);
    }

    public void addContent(Content contentToAdd) {
        Objects.requireNonNull(contentToAdd, "Content cannot be null");
        if (contents.contains(contentToAdd)) {
            throw new IllegalArgumentException("Content already exists");
        }
        contents.add(contentToAdd);
        notifyObservers(new AddContentEvent(contentToAdd));
    }

    public void removeContent(Content contentToRemove) {
        if (!contents.contains(contentToRemove)) {
            throw new IllegalArgumentException("Content does not exist");
        }
        contents.remove(contentToRemove);
        notifyObservers(new RemoveContentEvent(contentToRemove));
    }

    public void replaceContent(Content newContent) {
        Content oldContent = contents.stream()
                .filter(existingContent -> existingContent.equals(newContent))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("The content to be replaced does not exist in the platform.")
                );
        contents.remove(oldContent);
        contents.add(newContent);
        notifyObservers(new ReplaceContentEvent(oldContent, newContent));
    }

    private void notifyObservers(PlatformEvent event) {
        observers.forEach(observer -> observer.update(event));
    }
}
