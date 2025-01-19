package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

public class DisplayContentVisitor implements ContentVisitor<String> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String DEFAULT_DESCRIPTION = "No description available";
    private static final String DEFAULT_RELEASE_DATE = "Release date not specified";
    private static final String INDENT = "  ";

    @Override
    public String visitMovie(Movie movie) {
        return "Movie:\n" +
                displayCommonContentInfo(movie);
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        StringBuilder sb = new StringBuilder("TV Series:\n")
                .append(displayCommonContentInfo(tvSeries))
                .append("\n");

        IntStream.rangeClosed(1, tvSeries.getSeasonsCount())
                .forEach(seasonNumber -> {
                    sb.append(INDENT).append("Season ").append(seasonNumber).append(":\n")
                            .append(INDENT).append(INDENT).append("Episodes: ");

                    tvSeries.getEpisodesIterator(seasonNumber)
                            .forEachRemaining(episode -> sb.append(episode.getEpisodeNumber()).append(" "));
                    sb.append("\n");
                });

        return sb.toString();
    }

    private String displayCommonContentInfo(Content content) {
        return INDENT + "Title: " + content.getTitle() + "\n" +
                INDENT + "Description: " + content.getDescription().orElse(DEFAULT_DESCRIPTION) + "\n" +
                INDENT + "Release date: " +
                content.getReleaseDate().map(DATE_FORMATTER::format).orElse(DEFAULT_RELEASE_DATE) + "\n" +
                INDENT + "Duration: " + content.getDurationInMinutes() + " minutes" + "\n" +
                INDENT + "Requires subscription: " + (!content.isPremium() ? "No" : "Yes");
    }
}
