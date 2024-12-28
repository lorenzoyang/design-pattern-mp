package com.github.lorenzoyang.streaming;

import com.github.lorenzoyang.streaming.content.Episode;
import com.github.lorenzoyang.streaming.content.Video;
import com.github.lorenzoyang.streaming.content.model.Movie;
import com.github.lorenzoyang.streaming.content.model.TVSeries;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        TVSeries tv = new TVSeries.TVSeriesBuilder("One Piece", 1)
                .withDescription("A pirate adventure anime")
                .withReleaseDate(LocalDate.of(1999, 10, 20))
                .addEpisode(new Episode(1, 1440, new Video("episode1.mp4")))
                .build();
        System.out.println(tv);

        Movie movie = new Movie.MovieBuilder("Naruto", new Video("naruto.mp4"))
                .withDescription("A ninja adventure anime")
                .withReleaseDate(LocalDate.of(2002, 10, 3))
                .build();
        System.out.println(movie);
    }
}