package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.content.Season;
import com.github.lorenzoyang.freemediaplatform.content.TVSeries;

public class DownloadContentVisitor implements ContentVisitor<String> {
    private final String downloadPath;

    public DownloadContentVisitor(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public String visitMovie(Movie movie) {
        return "Successfully downloaded movie " + movie.getTitle() + " to: " + downloadPath + "/" + movie.getTitle()
                + movie.getResolution().map(resolution -> "." + resolution).orElse("");
    }

    @Override
    public String visitTVSeries(TVSeries tvSeries) {
        StringBuilder result = new StringBuilder();
        result.append("Successfully downloaded TV series ").append(tvSeries.getTitle()).append(" to: ")
                .append(downloadPath).append("/").append(tvSeries.getTitle())
                .append(tvSeries.getResolution().map(resolution -> "." + resolution).orElse(""));
        result.append("\nDownloaded the following:");
        for (Season season : tvSeries) {
            result.append("\n- Season ").append(season.getSeasonNumber())
                    .append(" with ").append(season.getEpisodesCount()).append(" episodes");
        }
        return result.toString();
    }
}