package com.github.lorenzoyang.streamingplatform.content;

public class MockContent extends Content {
    public MockContent() {
        super(new MockContentBuilder("Mock Content"));
    }

    public MockContent(String title) {
        super(new MockContentBuilder(title));
    }

    @Override
    public double getDurationMinutes() {
        return 0;
    }

    @Override
    protected ViewingProgress playContent(ViewingProgress currentProgress, double timeToWatch) {
        return currentProgress;
    }

    private static class MockContentBuilder extends ContentBuilder<MockContentBuilder> {
        private MockContentBuilder(String title) {
            super(title);
        }

        @Override
        protected MockContentBuilder self() {
            return this;
        }

        @Override
        public Content build() {
            throw new UnsupportedOperationException("MockContentBuilder cannot build Content");
        }
    }
}
