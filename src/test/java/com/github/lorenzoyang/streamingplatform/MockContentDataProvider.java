package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;

import java.time.LocalDate;
import java.util.List;

public class MockContentDataProvider implements DataProvider<Content> {
    private final List<Content> contents;

    public MockContentDataProvider() {
        this.contents = List.of(
                new TVSeries.TVSeriesBuilder("tvSeries1")
                        .requiresSubscription()
                        .withDescription("Description of tvSeries1")
                        .withReleaseDate(LocalDate.now())
                        .addEpisodes(1, List.of(
                                new Episode("episode1", 1, 26),
                                new Episode("episode2", 2, 26),
                                new Episode("episode3", 3, 26)
                        ))
                        .build(),
                new TVSeries.TVSeriesBuilder("tvSeries2")
                        .withDescription("Description of tvSeries2")
                        .withReleaseDate(LocalDate.now())
                        .addEpisodes(1, List.of(
                                new Episode("episode1", 1, 26),
                                new Episode("episode2", 2, 26),
                                new Episode("episode3", 3, 26)
                        ))
                        .addSeason(2)
                        .addEpisodes(2, List.of(
                                new Episode("episode1", 1, 26),
                                new Episode("episode2", 2, 26),
                                new Episode("episode3", 3, 26)
                        ))
                        .build(),
                new Movie.MovieBuilder("movie1", new Episode("episode1", 1, 26))
                        .withDescription("Description of movie1")
                        .withReleaseDate(LocalDate.now())
                        .build()
        );
    }

    @Override
    public List<Content> fetchData() {
        return contents;
    }
}
