package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.*;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DownloadContentVisitorTest {
    @Test
    public void testVisitMovieRunsCorrectly() {
        String downloadPath = "/downloads";
        Movie movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
                .withResolution(VideoResolution.FULL_HD_1080P)
                .build();
        String expected = "Successfully downloaded movie Movie to: /downloads/Movie.FULL_HD_1080P";
        assertEquals(expected, movie.accept(new DownloadContentVisitor(downloadPath)));
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        String downloadPath = "/downloads";
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TV Series",
                new Season(1, List.of(new Episode(1, 20))))
                .withResolution(VideoResolution.HD_720P)
                .build();
        String expected = "Successfully downloaded TV series TV Series to: /downloads/TV Series.HD_720P\n" +
                "Downloaded the following:\n" +
                "- Season 1 with 1 episodes";
        assertEquals(expected, tvSeries.accept(new DownloadContentVisitor(downloadPath)));
    }
}