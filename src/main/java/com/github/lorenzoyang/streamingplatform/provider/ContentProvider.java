package com.github.lorenzoyang.streamingplatform.provider;

import com.github.lorenzoyang.streamingplatform.content.Content;

import java.util.List;

public interface ContentProvider {
    List<Content> fetchContent();
}
