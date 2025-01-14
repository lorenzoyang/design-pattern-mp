package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

import java.time.format.DateTimeFormatter;

public class DisplayContentVisitor implements ContentVisitor<String> {
    private final DateTimeFormatter DATE_FORMATTER;

    public DisplayContentVisitor() {
        this(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public DisplayContentVisitor(DateTimeFormatter DATE_FORMATTER) {
        this.DATE_FORMATTER = DATE_FORMATTER;
    }

    @Override
    public String visitMovie(Movie movie) {
        return displayContent(movie) + "\n" +
                "Episode " + movie.getEpisode().getEpisodeNumber();
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        var builder = new StringBuilder();
        builder.append(displayContent(tvSeries)).append("\n");

        for (int seasonNumber = 1; seasonNumber <= tvSeries.getSeasonsCount(); seasonNumber++) {
            builder.append("Season ").append(seasonNumber).append(":\n");
            tvSeries.getEpisodes(seasonNumber).forEachRemaining(episode -> {
                builder.append("    Episode Number: ").append(episode.getEpisodeNumber()).append("\n")
                        .append("    Episode Duration: ").append(episode.getDurationMinutes()).append(" minutes\n");
            });
        }
        return builder.toString();
    }

    private String displayContent(Content content) {
        return "Title: " + content.getTitle() + "\n" +
                "Description: " + content.getDescription() + "\n" +
                "Release Date: " + content.getReleaseDate().format(DATE_FORMATTER) + "\n" +
                "Total duration: " + content.getDurationMinutes() + " minutes";
    }
}
