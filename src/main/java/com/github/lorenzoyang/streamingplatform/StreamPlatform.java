package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.user.User;

import java.util.Iterator;
import java.util.List;

public class StreamPlatform {
    private final String name;
    private final List<Content> contents;
    private final List<User> users;

    public StreamPlatform(String name, List<Content> contents, List<User> users) {
        this.name = name;
        this.contents = contents;
        this.users = users;
    }

    public User getUser(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }

    public Iterator<Content> getContentIterator() {
        return contents.iterator();
    }

    public Iterator<User> getUserIterator() {
        return users.iterator();
    }
}
