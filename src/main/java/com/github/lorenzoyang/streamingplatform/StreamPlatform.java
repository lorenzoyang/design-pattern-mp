package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;

import java.util.List;

public class StreamPlatform {
    private final String name;
    private final List<Content> contents;

    public StreamPlatform(String name, List<Content> contents) {
        this.name = name;
        this.contents = contents;
    }
}
