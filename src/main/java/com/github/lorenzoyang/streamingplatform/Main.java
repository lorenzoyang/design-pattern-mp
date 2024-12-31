package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("Naruto", VideoResolution.HD)
                .withDescription("Naruto is a Japanese manga series written and illustrated by Masashi Kishimoto.")
                .withReleaseDate(LocalDate.now())
                .addEpisode(new Episode(1, new Video("video1", 120)))
                .build();
        System.out.println(tvSeries);

        Movie movie = new Movie.MovieBuilder("The Matrix", new Video("video2", 150))
                .withDescription("A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.")
                .withReleaseDate(LocalDate.now())
                .build();
        System.out.println(movie);
    }
}