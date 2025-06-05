package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.utils.DisplayContentVisitor;
import com.github.lorenzoyang.freemediaplatform.utils.DownloadContentVisitor;

import java.util.*;

public class StreamingPlatform {
    private final String name;
    private final List<Content> contents;

    public StreamingPlatform(String name, List<Content> contents) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Platform name cannot be null or blank");
        }
        this.name = name;

        Objects.requireNonNull(contents, "Content list cannot be null");
        this.contents = new ArrayList<>(contents);
    }

    public String getName() {
        return name;
    }

    public Iterator<Content> contentIterator() {
        return contents.iterator();
    }

    public Optional<Content> getContentByTitle(String title) {
        return contents.stream()
                .filter(content -> content.getTitle().equals(title))
                .findFirst();
    }

    public String displayContent(Content content) {
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        return "From '" + getName() + "' platform:\n" + content.accept(new DisplayContentVisitor());
    }

    public String downloadContent(Content content, String downloadPath) {
        if (!contents.contains(content)) {
            throw new IllegalArgumentException("Content '" + content.getTitle() + "' does not exist");
        }
        if (downloadPath == null || downloadPath.isBlank()) {
            throw new IllegalArgumentException("Download path cannot be null or blank");
        }
        return content.accept(new DownloadContentVisitor(downloadPath));
    }
}
