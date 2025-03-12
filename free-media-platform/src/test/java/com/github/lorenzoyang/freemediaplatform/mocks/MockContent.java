package com.github.lorenzoyang.freemediaplatform.mocks;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.utils.ContentVisitor;

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
