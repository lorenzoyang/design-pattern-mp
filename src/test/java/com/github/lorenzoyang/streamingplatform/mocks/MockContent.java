package com.github.lorenzoyang.streamingplatform.mocks;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.utils.ContentVisitor;

public class MockContent extends Content {
    public MockContent(String title) {
        super(new MockContentBuilder(title));
    }

    private MockContent(MockContentBuilder builder) {
        super(builder);
    }

    @Override
    public int getDurationInMinutes() {
        return 0; // default implementation
    }

    @Override
    public <T> T accept(ContentVisitor<T> visitor) {
        return null;
    }

    public static class MockContentBuilder extends ContentBuilder<MockContentBuilder> {
        public MockContentBuilder(String title) {
            super(title);
        }

        @Override
        protected MockContentBuilder self() {
            return this;
        }

        @Override
        public Content build() {
            return new MockContent(this);
        }
    }
}
