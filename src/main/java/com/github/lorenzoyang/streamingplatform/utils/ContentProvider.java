package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;

import java.util.stream.Stream;

public interface ContentProvider<T extends Content> {
    Stream<T> retrieve();
}
