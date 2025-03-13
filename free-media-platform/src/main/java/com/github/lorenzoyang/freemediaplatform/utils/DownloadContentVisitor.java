package com.github.lorenzoyang.freemediaplatform.utils;

import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.content.TVSeries;

public class DownloadContentVisitor implements ContentVisitor<DownloadResult> {
    private final String downloadPath;

    public DownloadContentVisitor(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public DownloadResult visitMovie(Movie movie) {
        if (isInvalidDownloadPath()) {
            return new DownloadResult(false, "Invalid download path");
        }
        String msg = "Downloading movie " + movie.getTitle() + " to '" + downloadPath + "'";
        return new DownloadResult(true, msg);
    }

    @Override
    public DownloadResult visitTVSeries(TVSeries tvSeries) {
        if (isInvalidDownloadPath()) {
            return new DownloadResult(false, "Invalid download path");
        }
        String msg = "Downloading TV series " + tvSeries.getTitle() + " to '" + downloadPath + "'";
        return new DownloadResult(true, msg);
    }

    private boolean isInvalidDownloadPath() {
        return downloadPath == null || downloadPath.isBlank();
    }
}
