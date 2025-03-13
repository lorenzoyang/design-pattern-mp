package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DisplayContentVisitorTest {
    @Test
    public void testVisitMovieRunsCorrectly() {
        Content movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
                .withDescription("Movie description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .withResolution(VideoResolution.FULL_HD_1080P)
                .build();

        String expected = "Movie: Movie\n" +
                "  Description: Movie description\n" +
                "  Release Date: 01-01-2025\n" +
                "  Resolution: 1080p\n" +
                "  Total Duration: 120 minutes\n";

        assertEquals(expected, movie.accept(new DisplayContentVisitor()));
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        var season1 = new Season(1, List.of(
                new Episode(1, 20),
                new Episode(2, 20),
                new Episode(3, 20)));

        var season2 = new Season(2, List.of(
                new Episode(1, 20),
                new Episode(2, 20),
                new Episode(3, 20)));

        Content tvSeries = new TVSeries.TVSeriesBuilder("TVSeries", season1)
                .withDescription("TVSeries description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .withResolution(VideoResolution.FULL_HD_1080P)
                .withSeason(season2)
                .build();

        String expected = "TV Series: TVSeries\n" +
                "  Description: TVSeries description\n" +
                "  Release Date: 01-01-2025\n" +
                "  Resolution: 1080p\n" +
                "  Total Duration: 120 minutes\n" +
                "Season 1\n" +
                "  Total Duration: 60 minutes\n" +
                "  Episode 1, Duration: 20 minutes\n" +
                "  Episode 2, Duration: 20 minutes\n" +
                "  Episode 3, Duration: 20 minutes\n" +
                "Season 2\n" +
                "  Total Duration: 60 minutes\n" +
                "  Episode 1, Duration: 20 minutes\n" +
                "  Episode 2, Duration: 20 minutes\n" +
                "  Episode 3, Duration: 20 minutes\n";

        assertEquals(expected, tvSeries.accept(new DisplayContentVisitor()));
    }
}