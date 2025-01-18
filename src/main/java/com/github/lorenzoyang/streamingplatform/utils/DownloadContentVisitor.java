package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.user.User;

public class DownloadContentVisitor implements ContentVisitor<DownloadResult> {
    private final User user;

    public DownloadContentVisitor(User user) {
        this.user = user;
    }

    @Override
    public DownloadResult visitMovie(Movie movie) {
        String downloadMessage = "Downloading movie " + movie.getTitle() + "...";
        String accessDeniedMessage =
                "User '" + user.getUsername() + "' cannot download movie '" + movie.getTitle() + "'";

        return ensureUserCanDownloadContent(movie)
                ? new DownloadResult(true, downloadMessage)
                : new DownloadResult(false, accessDeniedMessage);
    }

    @Override
    public DownloadResult visitTVSeries(TVSeries tvSeries) {
        var downloadMessage = new StringBuilder();
        downloadMessage.append("Downloading TV series ").append(tvSeries.getTitle()).append("...\n");
        for (int i = 1; i <= tvSeries.getSeasonsCount(); i++) {
            downloadMessage.append("  Downloading season ").append(i).append("...\n");
            downloadMessage.append("    Downloading episodes: ");
            tvSeries.getEpisodesIterator(i).forEachRemaining(
                    episode -> downloadMessage.append(episode.getEpisodeNumber()).append(" ")
            );
        }
        String accessDeniedMessage =
                "User '" + user.getUsername() + "' cannot download TV series '" + tvSeries.getTitle() + "'";

        return ensureUserCanDownloadContent(tvSeries)
                ? new DownloadResult(true, downloadMessage.toString())
                : new DownloadResult(false, accessDeniedMessage);
    }

    private boolean ensureUserCanDownloadContent(Content content) {
        return user.hasSubscription() || content.isFree();
    }

}
