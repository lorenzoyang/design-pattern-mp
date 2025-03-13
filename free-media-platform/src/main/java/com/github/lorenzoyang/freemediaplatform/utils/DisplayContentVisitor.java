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
        var sb = new StringBuilder();
        sb.append("Movie: ").append(movie.getTitle()).append("\n");
        appendCommonContent(sb, movie);
        return sb.toString();
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        var sb = new StringBuilder();
        sb.append("TV Series: ").append(tvSeries.getTitle()).append("\n");
        appendCommonContent(sb, tvSeries);

        for (var season : tvSeries.getSeasons()) {
            sb.append("Season ").append(season.getSeasonNumber()).append("\n");
            sb.append(INDENT).append("Total Duration: ").append(season.getDurationInMinutes()).append(" minutes\n");

            for (var episode : season.getEpisodes()) {
                sb.append(INDENT)
                        .append("Episode ").append(episode.getEpisodeNumber())
                        .append(", Duration: ").append(episode.getDurationInMinutes())
                        .append(" minutes\n");
            }
        }
        return sb.toString();
    }


    private void appendCommonContent(StringBuilder sb, Content content) {
        String description = content.getDescription().orElse(DEFAULT_DESCRIPTION);
        String releaseDate = content.getReleaseDate()
                .map(DATE_FORMATTER::format)
                .orElse(DEFAULT_RELEASE_DATE);
        String resolution = content.getResolution()
                .map(VideoResolution::getDisplayName)
                .orElse("Resolution not specified");

        sb.append(INDENT).append("Description: ").append(description).append("\n");
        sb.append(INDENT).append("Release Date: ").append(releaseDate).append("\n");
        sb.append(INDENT).append("Resolution: ").append(resolution).append("\n");
        sb.append(INDENT).append("Total Duration: ").append(content.getDurationInMinutes()).append(" minutes\n");
    }
}
