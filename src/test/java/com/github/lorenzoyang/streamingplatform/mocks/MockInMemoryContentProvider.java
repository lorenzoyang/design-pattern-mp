package com.github.lorenzoyang.streamingplatform.mocks;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.utils.ContentProvider;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class MockInMemoryContentProvider implements ContentProvider<Content> {
    private final List<Content> contents;

    public MockInMemoryContentProvider() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        this.contents = List.of(
                new Movie.MovieBuilder("movie1", new Episode(1, 120))
                        .withDescription("Description of movie1")
                        .withReleaseDate(releaseDate)
                        .build(),
                new Movie.MovieBuilder("movie2", new Episode(1, 120))
                        .withDescription("Description of movie2")
                        .withReleaseDate(releaseDate)
                        .build(),
                new TVSeries.TVSeriesBuilder("tvSeries1")
                        .requiresSubscription()
                        .withDescription("Description of tvSeries1")
                        .withReleaseDate(releaseDate)
                        .addEpisodes(1, List.of(
                                new Episode(1, 26),
                                new Episode(2, 26)
                        ))
                        .build()
        );
    }

    @Override
    public Stream<Content> retrieve() {
        return contents.stream();
    }
}
