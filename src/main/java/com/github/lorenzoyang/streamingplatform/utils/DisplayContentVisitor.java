package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

import java.time.format.DateTimeFormatter;

public class DisplayContentVisitor implements ContentVisitor<String> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String DEFAULT_DESCRIPTION = "No description available";
    private static final String DEFAULT_RELEASE_DATE = "Release date not specified";

    @Override
    public String visitMovie(Movie movie) {
        return "Movie:\n" + displayCommonContentInfo(movie);
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        var sb = new StringBuilder();
        sb.append("TV Series:\n").append(displayCommonContentInfo(tvSeries)).append("\n");

        for (int i = 1; i <= tvSeries.getSeasonsCount(); i++) {
            sb.append("  Season ").append(i).append(":\n");
            sb.append("    Episodes: ");
            tvSeries.getEpisodesIterator(i).forEachRemaining(
                    episode -> sb.append(episode.getEpisodeNumber()).append(" ")
            );
        }
        return sb.toString();
    }

    private String displayCommonContentInfo(Content content) {
        String releaseDate = content.getReleaseDate()
                .map(DATE_FORMATTER::format)
                .orElse(DEFAULT_RELEASE_DATE);

        return "  Title: " + content.getTitle() + "\n" +
                "  Description: " + content.getDescription().orElse(DEFAULT_DESCRIPTION) + "\n" +
                "  Release date: " + releaseDate + "\n" +
                "  Duration: " + content.getDurationInMinutes() + " minutes" + "\n" +
                "  Requires subscription: " + (content.isPremium() ? "No" : "Yes");
    }
}
