package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.content.TVSeries;

public interface ContentVisitor<T> {
    T visitMovie(Movie movie);

    T visitTVSeries(TVSeries tvSeries);
}
