package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.*;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlaybackContentVisitorTest {
    @Test
    public void testVisitMovieRunsCorrectly() {
        var episode = new Episode(1, 120);
        Content movie = new Movie.MovieBuilder("Movie", episode).build();
        Iterable<Episode> episodes = movie.accept(new PlaybackContentVisitor());

        assertThat(episodes).containsExactly(episode);
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        var episode1 = new Episode(1, 20);
        var episode2 = new Episode(2, 20);
        var season1 = new Season(1, List.of(episode1, episode2));
        var episode3 = new Episode(1, 20);
        var season2 = new Season(2, List.of(episode3));
        Content tvSeries = new TVSeries.TVSeriesBuilder("TVSeries", season1)
                .withSeason(season2)
                .build();
        Iterable<Episode> episodes = tvSeries.accept(new PlaybackContentVisitor());

        assertThat(episodes).containsExactly(episode1, episode2, episode3);
    }
}