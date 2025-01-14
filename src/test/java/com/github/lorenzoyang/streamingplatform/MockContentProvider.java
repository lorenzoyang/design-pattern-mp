package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.contents.Content;
import com.github.lorenzoyang.streamingplatform.contents.Episode;
import com.github.lorenzoyang.streamingplatform.contents.Movie;
import com.github.lorenzoyang.streamingplatform.contents.TVSeries;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;

import java.time.LocalDate;
import java.util.List;

public class MockContentProvider implements DataProvider<Content> {
    private final List<Content> contents;

    public MockContentProvider() {
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
    public List<Content> fetchData() {
        return contents;
    }
}
