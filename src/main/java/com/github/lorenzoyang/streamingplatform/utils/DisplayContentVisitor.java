package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.contents.Content;
import com.github.lorenzoyang.streamingplatform.contents.Movie;
import com.github.lorenzoyang.streamingplatform.contents.Season;
import com.github.lorenzoyang.streamingplatform.contents.TVSeries;

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
        var sb = new StringBuilder();
        sb.append(displayContent(tvSeries)).append("\n");

        for (Season season : tvSeries.getSeasons()) {
            sb.append("Season ").append(season.getSeasonNumber()).append(": ");
            season.getEpisodes().forEachRemaining(episode -> {
                sb.append("  Episode Number: ").append(episode.getEpisodeNumber()).append("\n")
                        .append("  Episode Duration: ").append(episode.getDurationMinutes()).append(" minutes\n");
            });
        }
        return sb.toString();
    }

    private String displayContent(Content content) {
        return "Title: " + content.getTitle() + "\n" +
                "Description: " + content.getDescription() + "\n" +
                "Release Date: " + content.getReleaseDate().format(DATE_FORMATTER) + "\n" +
                "Total duration: " + content.getDurationMinutes() + " minutes";
    }
}
