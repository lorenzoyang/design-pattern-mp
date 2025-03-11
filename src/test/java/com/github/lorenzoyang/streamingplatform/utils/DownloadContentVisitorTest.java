//package com.github.lorenzoyang.streamingplatform.utils;
//
//import com.github.lorenzoyang.streamingplatform.content.Content;
//import com.github.lorenzoyang.streamingplatform.content.Episode;
//import com.github.lorenzoyang.streamingplatform.content.Movie;
//import com.github.lorenzoyang.streamingplatform.content.TVSeries;
//import com.github.lorenzoyang.streamingplatform.user.User;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class DownloadContentVisitorTest {
//    private User user;
//    private User subscribedUser;
//
//    @Before
//    public void setUp() {
//        this.user = new User.UserBuilder("username", "password").build();
//        this.subscribedUser = new User.UserBuilder("subscribedUser", "password")
//                .subscribe()
//                .build();
//    }
//
//    @Test
//    public void testVisitMovieRunsCorrectly() {
//        Content movie = new Movie.MovieBuilder("Movie", new Episode(1, 120))
//                .withPremium()
//                .withDescription("Movie description")
//                .withReleaseDate(LocalDate.of(2025, 1, 1))
//                .build();
//
//        DownloadResult result = movie.accept(new DownloadContentVisitor(subscribedUser));
//        String expected = "User 'subscribedUser': Downloading movie 'Movie'...";
//        assertTrue(result.isSuccess());
//        assertEquals(expected, result.getMessage());
//
//        result = movie.accept(new DownloadContentVisitor(user));
//        expected = "User 'username' cannot download movie 'Movie'";
//        assertFalse(result.isSuccess());
//        assertEquals(expected, result.getMessage());
//    }
//
//    @Test
//    public void testVisitTVSeriesRunsCorrectly() {
//        List<Episode> episodes = List.of(
//                new Episode(1, 20),
//                new Episode(2, 20),
//                new Episode(3, 20)
//        );
//        Content tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
//                .withPremium()
//                .withDescription("TVSeries description")
//                .withReleaseDate(LocalDate.of(2025, 1, 1))
//                .addEpisodes(1, episodes)
//                .withSeason(2)
//                .addEpisodes(2, episodes)
//                .build();
//
//        DownloadResult result = tvSeries.accept(new DownloadContentVisitor(subscribedUser));
//        String expected = "User 'subscribedUser': Downloading TV series 'TVSeries'...\n" +
//                "  Downloading season 1...\n" +
//                "    Downloading episodes: 1 2 3 \n" +
//                "  Downloading season 2...\n" +
//                "    Downloading episodes: 1 2 3 \n";
//        assertTrue(result.isSuccess());
//        assertEquals(expected, result.getMessage());
//
//        result = tvSeries.accept(new DownloadContentVisitor(user));
//        expected = "User 'username' cannot download TV series 'TVSeries'";
//        assertFalse(result.isSuccess());
//        assertEquals(expected, result.getMessage());
//    }
//}