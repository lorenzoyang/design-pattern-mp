package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.contents.Content;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;
import com.github.lorenzoyang.streamingplatform.utils.DisplayContentVisitor;
import com.github.lorenzoyang.streamingplatform.utils.DownloadContentVisitor;
import com.github.lorenzoyang.streamingplatform.utils.DownloadResult;

import java.util.*;

public final class StreamingPlatform {
    private final String name;

    private final List<Content> contents;
    private final List<User> users;

    private final Map<User, List<DownloadResult>> downloadHistory;

    public StreamingPlatform(String name, DataProvider<Content> contentProvider) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");

        Objects.requireNonNull(contentProvider, "Content provider cannot be null");
        this.contents = new ArrayList<>(contentProvider.fetchData());

        this.users = new ArrayList<>();

        this.downloadHistory = new HashMap<>();
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

    public String displayContent(Content content) {
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content does not exist in the platform");
        }
        return content.accept(new DisplayContentVisitor());
    }

    public DownloadResult downloadContent(User user, Content content) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User not yet registered");
        }
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content does not exist in the platform");
        }
        DownloadResult result = content.accept(new DownloadContentVisitor(user));
        downloadHistory.computeIfAbsent(user, k -> new ArrayList<>()).add(result);
        return result;
    }

    public Iterator<DownloadResult> getDownloadHistory(User user) {
        if (!users.contains(user)) {
            throw new IllegalArgumentException("User not yet registered");
        }
        return downloadHistory.getOrDefault(user, Collections.emptyList()).iterator();
    }

    public String getName() {
        return name;
    }

    // package-private getter for testing purposes
    Map<User, List<DownloadResult>> getDownloadHistory() {
        return downloadHistory;
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
