package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.content.Episode;
import com.github.lorenzoyang.freemediaplatform.events.AddContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.PlatformEvent;
import com.github.lorenzoyang.freemediaplatform.events.RemoveContentEvent;
import com.github.lorenzoyang.freemediaplatform.events.UpdateContentEvent;
import com.github.lorenzoyang.freemediaplatform.exceptions.StreamingPlatformException;
import com.github.lorenzoyang.freemediaplatform.utils.DisplayContentVisitor;
import com.github.lorenzoyang.freemediaplatform.utils.PlaybackContentVisitor;

import java.util.*;
import java.util.function.Supplier;

public class StreamingPlatform {
    private final String name;
    private final Collection<Content> contents;
    private final Collection<PlatformObserver> observers;

    public StreamingPlatform(String name, Supplier<Collection<Content>> contentProvider) {
        Objects.requireNonNull(name, "Streaming platform name cannot be null");
        if (name.isBlank()) {
            throw new StreamingPlatformException("Streaming platform name cannot be blank");
        }
        this.name = name;

        Objects.requireNonNull(contentProvider, "Content provider cannot be null");
        this.contents = new ArrayList<>(contentProvider.get());

        this.observers = new ArrayList<>();
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

    public String displayContent(Content content) {
        Objects.requireNonNull(content, "Content cannot be null");
        if (!contents.contains(content)) {
            throw new StreamingPlatformException("Content '" + content.getTitle() + "' does not exist");
        }
        return "From '" + getName() + "' platform:\n" + content.accept(new DisplayContentVisitor());
    }

    public Iterable<Episode> watchContent(Content content) {
        Objects.requireNonNull(content, "Content cannot be null");
        if (!contents.contains(content)) {
            throw new StreamingPlatformException("Content '" + content.getTitle() + "' does not exist");
        }
        return content.accept(new PlaybackContentVisitor());
    }

    public void addObserver(PlatformObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(PlatformObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(PlatformEvent event) {
        observers.forEach(observer -> observer.notifyChange(event));
    }

    // package-private getter for testing purposes
    Collection<Content> getContents() {
        return Collections.unmodifiableCollection(contents);
    }

    // package-private getter for testing purposes
    Collection<PlatformObserver> getObservers() {
        return Collections.unmodifiableCollection(observers);
    }
}
