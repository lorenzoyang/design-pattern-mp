package com.github.lorenzoyang.streamingplatform.utils;

import com.github.lorenzoyang.streamingplatform.User;
import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.Movie;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class DownloadContentVisitorTest {
    private User.UserBuilder userBuilder;

    @Before
    public void setUp() {
        this.userBuilder = new User.UserBuilder("username", "password");
    }

    @Test
    public void testVisitMovieRunsCorrectly() {
        User user = userBuilder
                .subscribe()
                .build();
        Content content = new Movie.MovieBuilder(
                "movie",
                new Episode("episode", 1, 120)
        )
                .requiresSubscription()
                .build();
        DownloadContentVisitor downloadContentVisitor = new DownloadContentVisitor(user);
        DownloadResult result = content.accept(downloadContentVisitor);

        String expectedMsg = "Downloading movie: movie";
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo(expectedMsg);
    }

    @Test
    public void testVisitTVSeriesRunsCorrectly() {
        User user = userBuilder
                .subscribe()
                .build();
        Content content = new TVSeries.TVSeriesBuilder("tvSeries")
                .addEpisodes(1, List.of(
                        new Episode("episode1", 1, 20),
                        new Episode("episode2", 2, 20)
                ))
                .addSeason(2)
                .addEpisodes(2, List.of(
                        new Episode("episode3", 1, 20),
                        new Episode("episode4", 2, 20)
                ))
                .requiresSubscription()
                .build();
        DownloadContentVisitor downloadContentVisitor = new DownloadContentVisitor(user);
        DownloadResult result = content.accept(downloadContentVisitor);

        String expectedMsg = "Downloading TV series: tvSeries\n" +
                "    Downloading episode 1: episode1\n" +
                "    Downloading episode 2: episode2\n" +
                "    Downloading episode 1: episode3\n" +
                "    Downloading episode 2: episode4\n";
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getMessage()).isEqualTo(expectedMsg);
    }
}