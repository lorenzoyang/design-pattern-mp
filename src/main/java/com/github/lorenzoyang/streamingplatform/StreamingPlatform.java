package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.AddContentEvent;
import com.github.lorenzoyang.streamingplatform.events.PlatformEvent;
import com.github.lorenzoyang.streamingplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.streamingplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.PlatformObserver;

import java.util.*;

public final class StreamingPlatform {
    private final String name;

    private final List<Content> contents;
    private final List<User> users;

    private final Collection<PlatformObserver> observers;

    public StreamingPlatform(String name, DataProvider<Content> contentProvider) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");

        Objects.requireNonNull(contentProvider, "Content provider cannot be null");
        this.contents = new ArrayList<>(contentProvider.fetchData());

        this.users = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void addObserver(PlatformObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(PlatformObserver observer) {
        observers.remove(observer);
    }

    public void registerUser(User user) {
        Objects.requireNonNull(user, "User cannot be null");
        if (users.contains(user)) {
            throw new IllegalArgumentException("User already registered");
        }
        users.add(user);
        addObserver(user);
    }

    public void unregisterUser(User user) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User does not exist");
        }
        users.remove(user);
        removeObserver(user);
    }

    public Optional<Content> getContentByTitle(String title) {
        return contents.stream()
                .filter(content -> content.getTitle().equals(title))
                .findFirst();
    }

    public Optional<User> getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
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

    public void updateContent(Content updatedContent) {
        Content oldContent = contents.stream()
                .filter(existingContent -> existingContent.equals(updatedContent))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("The content to be updated does not exist in the platform.")
                );
        contents.remove(oldContent);
        contents.add(updatedContent);
        notifyObservers(new UpdateContentEvent(oldContent, updatedContent));
    }

    private void notifyObservers(PlatformEvent event) {
        observers.forEach(observer -> observer.update(event));
    }

    public String getName() {
        return name;
    }

    // package-private getter for testing purposes
    Collection<PlatformObserver> getObservers() {
        return observers;
    }

    // package-private getter for testing purposes
    List<Content> getContents() {
        return contents;
    }

    // package-private getter for testing purposes
    List<User> getUsers() {
        return users;
    }

}
