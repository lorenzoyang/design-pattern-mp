package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

import java.util.Iterator;

public class DisplayContentVisitor implements ContentVisitor<String> {
    @Override
    public String visitMovie(Movie movie) {
        return displayContent(movie) + "\n" +
                "Episode: " + movie.getEpisode().getTitle() + "\n" +
                "Episode Number: " + movie.getEpisode().getEpisodeNumber() + "\n" +
                "Duration: " + movie.getEpisode().getDurationMinutes() + " minutes";
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        StringBuilder builder = new StringBuilder();
        builder.append(displayContent(tvSeries)).append("\n")
                .append("Total Duration: ").append(tvSeries.getDurationMinutes()).append(" minutes").append("\n");

        for (int seasonNumber = 1; seasonNumber <= tvSeries.getSeasonsCount(); seasonNumber++) {
            builder.append("Season ").append(seasonNumber).append(":\n");
            Iterator<Episode> episodes = tvSeries.getEpisodes(seasonNumber);
            while (episodes.hasNext()) {
                Episode episode = episodes.next();
                builder.append("  Episode: ").append(episode.getTitle()).append("\n")
                        .append("  Episode Number: ").append(episode.getEpisodeNumber()).append("\n")
                        .append("  Duration: ").append(episode.getDurationMinutes()).append(" minutes\n");
            }
        }
        return builder.toString();
    }

    private String displayContent(Content content) {
        return "Title: " + content.getTitle() + "\n" +
                "Description: " + content.getDescription() + "\n" +
                "Release Date: " + content.getReleaseDate();
    }
}
