package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.contents.Movie;
import com.github.lorenzoyang.streamingplatform.contents.TVSeries;

public interface ContentVisitor<T> {
    T visitMovie(Movie movie);

    T visitTVSeries(TVSeries tvSeries);
}
