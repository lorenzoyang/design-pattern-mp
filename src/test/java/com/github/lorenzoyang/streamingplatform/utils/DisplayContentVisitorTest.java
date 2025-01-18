package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DisplayContentVisitorTest {
    @Test
    public void testVisitMovieRunsCorrectly() {
        Content movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
                .requiresSubscription()
                .withDescription("Movie description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .build();

        String expected = "Movie:\n" +
                "  Title: Movie\n" +
                "  Description: Movie description\n" +
                "  Release date: 01-01-2025\n" +
                "  Duration: 120 minutes\n" +
                "  Requires subscription: Yes";
        assertEquals(expected, movie.accept(new DisplayContentVisitor()));
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        Content tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .addEpisodes(1, List.of(
                        new Episode(1, 20),
                        new Episode(2, 20),
                        new Episode(3, 20))
                )
                .withDescription("TVSeries description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .build();

        String expected = "TV Series:\n" +
                "  Title: TVSeries\n" +
                "  Description: TVSeries description\n" +
                "  Release date: 01-01-2025\n" +
                "  Duration: 60 minutes\n" +
                "  Requires subscription: No\n" +
                "  Season 1:\n" +
                "    Episodes: 1 2 3 ";
        assertEquals(expected, tvSeries.accept(new DisplayContentVisitor()));
    }
}