package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Episode;
import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.content.Season;
import com.github.lorenzoyang.freemediaplatform.content.TVSeries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlaybackContentVisitor implements ContentVisitor<Iterable<Episode>> {
    @Override
    public Iterable<Episode> visitMovie(Movie movie) {
        return List.of(movie.getEpisode());
    }

    @Override
    public Iterable<Episode> visitTVSeries(TVSeries tvSeries) {
        Collection<Episode> allEpisodes = new ArrayList<>();
        for (Season season : tvSeries) {
            season.iterator().forEachRemaining(allEpisodes::add);
        }
        return allEpisodes;
    }
}
