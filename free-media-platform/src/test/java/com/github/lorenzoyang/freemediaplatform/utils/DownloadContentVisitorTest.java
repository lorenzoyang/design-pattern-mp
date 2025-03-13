package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.*;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class DownloadContentVisitorTest {
    @Test
    public void testVisitMovieRunsCorrectly() {
        Content movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
                .withDescription("Movie description")
                .withReleaseDate(LocalDate.of(2025, 1, 1))
                .withResolution(VideoResolution.FULL_HD_1080P)
                .build();

        DownloadResult result = movie.accept(new DownloadContentVisitor("path"));
        String expected = "Downloading movie Movie to 'path'";
        assertTrue(result.isSuccess());
        assertEquals(expected, result.getMessage());

        result = movie.accept(new DownloadContentVisitor(null));
        expected = "Invalid download path";
        assertFalse(result.isSuccess());
        assertEquals(expected, result.getMessage());

        result = movie.accept(new DownloadContentVisitor(""));
        assertFalse(result.isSuccess());
        assertEquals(expected, result.getMessage());
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

        DownloadResult result = tvSeries.accept(new DownloadContentVisitor("path"));
        String expected = "Downloading TV series TVSeries to 'path'";
        assertTrue(result.isSuccess());
        assertEquals(expected, result.getMessage());

        result = tvSeries.accept(new DownloadContentVisitor(null));
        expected = "Invalid download path";
        assertFalse(result.isSuccess());
        assertEquals(expected, result.getMessage());

        result = tvSeries.accept(new DownloadContentVisitor(""));
        assertFalse(result.isSuccess());
        assertEquals(expected, result.getMessage());
    }

}