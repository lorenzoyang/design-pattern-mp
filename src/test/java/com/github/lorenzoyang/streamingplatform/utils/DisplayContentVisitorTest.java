package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class DisplayContentVisitorTest {
    private DisplayContentVisitor displayContentVisitor;
    private LocalDate releaseDate;

    @Before
    public void setUp() {
        displayContentVisitor = new DisplayContentVisitor();
        this.releaseDate = LocalDate.of(2025, 1, 1);
    }

    @Test
    public void testVisitMovieRunsCorrectly() {
        Content content = new Movie.MovieBuilder("movie", new Episode("episode", 1, 120))
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .requiresSubscription()
                .build();

        String output = content.accept(displayContentVisitor);
        String expectedOutput = "Title: movie\n" +
                "Description: description\n" +
                "Release Date: 01 Jan 2025\n" +
                "Total duration: 120 minutes\n" +
                "Episode Title: episode";

        assertThat(output).isEqualTo(expectedOutput);
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        Content content = new TVSeries.TVSeriesBuilder("tvSeries")
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .addEpisodes(1, List.of(
                        new Episode("episode1", 1, 20),
                        new Episode("episode2", 2, 20)
                ))
                .addSeason(2)
                .addEpisodes(2, List.of(
                        new Episode("episode3", 1, 20),
                        new Episode("episode4", 2, 20)
                ))
                .build();

        String output = content.accept(displayContentVisitor);
        String expectedOutput = "Title: tvSeries\n" +
                "Description: description\n" +
                "Release Date: 01 Jan 2025\n" +
                "Total duration: 80 minutes\n" +
                "Season 1:\n" +
                "    Episode Number: 1\n" +
                "    Episode Title: episode1\n" +
                "    Episode Duration: 20 minutes\n" +
                "    Episode Number: 2\n" +
                "    Episode Title: episode2\n" +
                "    Episode Duration: 20 minutes\n" +
                "Season 2:\n" +
                "    Episode Number: 1\n" +
                "    Episode Title: episode3\n" +
                "    Episode Duration: 20 minutes\n" +
                "    Episode Number: 2\n" +
                "    Episode Title: episode4\n" +
                "    Episode Duration: 20 minutes\n";

        assertThat(output).isEqualTo(expectedOutput);
    }
}