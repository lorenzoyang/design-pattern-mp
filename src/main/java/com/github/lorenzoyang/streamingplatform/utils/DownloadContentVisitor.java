package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.User;
import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;

public class DownloadContentVisitor implements ContentVisitor<DownloadResult> {
    private final User user;

    public DownloadContentVisitor(User user) {
        this.user = user;
    }

    @Override
    public DownloadResult visitMovie(Movie movie) {
        return ensureUserCanDownloadContent(movie)
                ? new DownloadResult(true, "Downloading movie: " + movie.getTitle())
                : new DownloadResult(false, "You need a subscription to download this movie");
    }

    @Override
    public DownloadResult visitTVSeries(TVSeries tvSeries) {
        if (!ensureUserCanDownloadContent(tvSeries)) {
            return new DownloadResult(false, "You need a subscription to download this TV series");
        }

        StringBuilder sb = new StringBuilder("Downloading TV series: ")
                .append(tvSeries.getTitle()).append("\n");

        for (int seasonNumber = 1; seasonNumber <= tvSeries.getSeasonsCount(); seasonNumber++) {
            tvSeries.getEpisodes(seasonNumber).forEachRemaining(episode ->
                    sb.append("    Downloading episode ")
                            .append(episode.getEpisodeNumber())
                            .append("\n")
            );
        }
        return new DownloadResult(true, sb.toString());
    }

    private boolean ensureUserCanDownloadContent(Content content) {
        return user.hasSubscription() || content.isFree();
    }
}
