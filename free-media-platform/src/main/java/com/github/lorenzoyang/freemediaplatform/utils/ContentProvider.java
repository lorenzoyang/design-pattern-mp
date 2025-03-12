package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Content;

import java.util.stream.Stream;

public interface ContentProvider<T extends Content> {
    Stream<T> retrieve();
}
