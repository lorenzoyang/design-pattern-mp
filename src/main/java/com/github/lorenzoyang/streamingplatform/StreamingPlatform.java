package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.events.*;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.user.User;
import com.github.lorenzoyang.streamingplatform.utils.ContentProvider;
import com.github.lorenzoyang.streamingplatform.utils.DisplayContentVisitor;
import com.github.lorenzoyang.streamingplatform.utils.DownloadContentVisitor;
import com.github.lorenzoyang.streamingplatform.utils.DownloadResult;

import java.util.*;
import java.util.stream.Collectors;

public final class StreamingPlatform {
    private final String name;
    private final List<Content> contents;
    private final List<User> users;
    private final Collection<PlatformObserver> observers;
    private final Map<User, List<DownloadResult>> downloadHistory;

    public StreamingPlatform(String name, ContentProvider<Content> contentProvider) {
        this.name = Objects.requireNonNull(name, "Streaming platform name cannot be null");

        Objects.requireNonNull(contentProvider, "Content provider cannot be null");
        this.contents = contentProvider.retrieve()
                .collect(Collectors.toList());

        this.users = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.downloadHistory = new HashMap<>();
    }

    public boolean registerUser(User user) {
        if (users.contains(user)) {
            return false;
        }
        return users.add(user);
    }

    public boolean unregisterUser(User user) {
        if (!users.contains(user)) {
            return false;
        }
        return users.remove(user);
    }

    public boolean addContent(Content newContent) {
        if (contents.contains(newContent)) {
            return false;
        }
        contents.add(newContent);
        notifyObservers(new AddContentEvent(newContent));
        return true;
    }

    public boolean removeContent(Content existingContent) {
        if (!contents.contains(existingContent)) {
            return false;
        }
        contents.remove(existingContent);
        notifyObservers(new RemoveContentEvent(existingContent));
        return true;
    }

    public boolean updateContent(Content updatedContent) {
        Optional<Content> oldContent = contents.stream()
                .filter(existingContent -> existingContent.equals(updatedContent))
                .findFirst();

        if (oldContent.isEmpty()) {
            return false;
        }

        contents.remove(oldContent.get());
        contents.add(updatedContent);
        notifyObservers(new UpdateContentEvent(oldContent.get(), updatedContent));
        return true;
    }

    public int watchContent(User user, Content content, int timeToWatch) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is not registered");
        }
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        if (!user.hasSubscription() && content.isPremium()) {
            throw new AccessDeniedException("User '" + user.getUsername() + "' does not have a subscription");
        }
        if (timeToWatch <= 0) {
            throw new IllegalArgumentException("Time to watch must be greater than 0");
        }
        notifyObservers(new WatchContentEvent(user, content, timeToWatch));

        return Math.min(timeToWatch, content.getDurationInMinutes()); // return effective time watched
    }

    public String displayContent(Content content) {
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        return "From '" + getName() + "' platform:\n" + content.accept(new DisplayContentVisitor());
    }

    public DownloadResult downloadContent(User user, Content content) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is not registered");
        }
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        DownloadResult result = content.accept(new DownloadContentVisitor(user));
        downloadHistory.computeIfAbsent(user, u -> new ArrayList<>()).add(result);
        return result;
    }

    public Iterator<DownloadResult> getDownloadHistoryIterator(User user) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User '" + user.getUsername() + "' is not registered");
        }
        return downloadHistory.getOrDefault(user, Collections.emptyList()).iterator();
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

    public boolean addObserver(PlatformObserver observer) {
        if (observers.contains(observer)) {
            return false;
        }
        return observers.add(observer);
    }

    public boolean removeObserver(PlatformObserver observer) {
        if (!observers.contains(observer)) {
            return false;
        }
        return observers.remove(observer);
    }

    private void notifyObservers(PlatformEvent event) {
        observers.forEach(observer -> observer.notifyChange(event));
    }

    // package-private getter for testing purposes
    List<User> getUsers() {
        return users;
    }

    // package-private getter for testing purposes
    List<Content> getContents() {
        return contents;
    }

    // package-private getter for testing purposes
    Collection<PlatformObserver> getObservers() {
        return observers;
    }

    public String getName() {
        return name;
    }
}
