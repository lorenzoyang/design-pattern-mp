package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

public interface ContentVisitor<T> {
    T visitMovie(Movie movie);

    T visitTVSeries(TVSeries tvSeries);
}
