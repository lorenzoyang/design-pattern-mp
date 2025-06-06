package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.content.TVSeries;
import com.github.lorenzoyang.freemediaplatform.content.VideoResolution;

import java.time.format.DateTimeFormatter;

public class DisplayContentVisitor implements ContentVisitor<String> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String DEFAULT_DESCRIPTION = "No description available";
    private static final String DEFAULT_RELEASE_DATE = "Release date not specified";
    private static final String INDENT = "  ";

    @Override
    public String visitMovie(Movie movie) {
        return String.format("Movie: %s\n%s",
                movie.getTitle(),
                formatCommonContent(movie));
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        var sb = new StringBuilder();
        sb.append("TV Series: ").append(tvSeries.getTitle()).append("\n");
        sb.append(formatCommonContent(tvSeries));

        tvSeries.forEach(season -> {
            sb.append("Season ").append(season.getSeasonNumber()).append("\n");
            sb.append(INDENT).append("Total Duration: ").append(season.getDurationInMinutes()).append(" minutes\n");

            season.forEach(episode -> sb.append(String.format("%sEpisode %d, Duration: %d minutes\n",
                    INDENT, episode.getEpisodeNumber(), episode.getDurationInMinutes())));
        });

        return sb.toString();
    }


    private String formatCommonContent(Content content) {
        String description = content.getDescription().orElse(DEFAULT_DESCRIPTION);
        String releaseDate = content.getReleaseDate()
                .map(DATE_FORMATTER::format)
                .orElse(DEFAULT_RELEASE_DATE);
        String resolution = content.getResolution()
                .map(VideoResolution::getDisplayName)
                .orElse("Resolution not specified");

        return String.format("%sDescription: %s\n" +
                        "%sRelease Date: %s\n" +
                        "%sResolution: %s\n" +
                        "%sTotal Duration: %d minutes\n",
                INDENT, description,
                INDENT, releaseDate,
                INDENT, resolution,
                INDENT, content.getDurationInMinutes()
        );
    }
}
