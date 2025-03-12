//package com.github.lorenzoyang.freemediaplatform.utils;
//
//import com.github.lorenzoyang.freemediaplatform.content.Movie;
//import com.github.lorenzoyang.freemediaplatform.content.TVSeries;
//import com.github.lorenzoyang.freemediaplatform.user.User;
//
//import java.util.stream.IntStream;
//
//public class DownloadContentVisitor implements ContentVisitor<DownloadResult> {
//    private final User user;
//    private static final String INDENT = "  ";
//
//    public DownloadContentVisitor(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public DownloadResult visitMovie(Movie movie) {
//        String downloadMsg = "User '" + user.getUsername() + "': Downloading movie '" + movie.getTitle() + "'...";
//        String accessDeniedMsg = "User '" + user.getUsername() + "' cannot download movie '" + movie.getTitle() + "'";
//
//        return user.hasSubscription()
//                ? new DownloadResult(true, downloadMsg)
//                : new DownloadResult(false, accessDeniedMsg);
//    }
//
//    @Override
//    public DownloadResult visitTVSeries(TVSeries tvSeries) {
//        if (!user.hasSubscription()) {
//            String accessDeniedMsg = "User '" + user.getUsername() +
//                    "' cannot download TV series '" + tvSeries.getTitle() + "'";
//
//            return new DownloadResult(false, accessDeniedMsg);
//        }
//
//        var sb = new StringBuilder("User '" + user.getUsername() + "': Downloading TV series '")
//                .append(tvSeries.getTitle()).append("'...\n");
//        IntStream.rangeClosed(1, tvSeries.getSeasonsCount())
//                .forEach(seasonNumber -> {
//                    sb.append(INDENT).append("Downloading season ").append(seasonNumber).append("...\n");
//                    sb.append(INDENT).append(INDENT).append("Downloading episodes: ");
//                    tvSeries.getEpisodesIterator(seasonNumber)
//                            .forEachRemaining(episode -> sb.append(episode.getEpisodeNumber()).append(" "));
//                    sb.append("\n");
//                });
//
//        return new DownloadResult(true, sb.toString());
//    }
//}
