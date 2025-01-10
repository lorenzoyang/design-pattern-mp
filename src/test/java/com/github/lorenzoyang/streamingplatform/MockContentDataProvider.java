package com.github.lorenzoyang.streamingplatform;

import com.github.lorenzoyang.streamingplatform.content.Content;
import com.github.lorenzoyang.streamingplatform.content.Episode;
import com.github.lorenzoyang.streamingplatform.content.TVSeries;
import com.github.lorenzoyang.streamingplatform.content.video.Video;
import com.github.lorenzoyang.streamingplatform.content.video.VideoResolution;
import com.github.lorenzoyang.streamingplatform.utils.DataProvider;

import java.time.LocalDate;
import java.util.List;

public class MockContentDataProvider implements DataProvider<Content> {
    @Override
    public List<Content> fetchData() {
        return List.of(
                new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                        .requiresSubscription()
                        .withDescription("Description of tvSeries1")
                        .withReleaseDate(LocalDate.now())
                        .withSeason(1)
                        .addEpisode(
                                new Episode(
                                        "episode1",
                                        1,
                                        new Video("video1.mp4", 25)
                                )
                        )
                        .addEpisode(
                                new Episode(
                                        "episode2",
                                        2,
                                        new Video("video2.mp4", 25)
                                )
                        )
                        .addEpisode(
                                new Episode(
                                        "episode3",
                                        3,
                                        new Video("video3.mp4", 25)
                                )
                        )
                        .build(),
                new TVSeries.TVSeriesBuilder("tvSeries2", VideoResolution.FHD)
                        .withDescription("Description of tvSeries2")
                        .withReleaseDate(LocalDate.now())
                        .withSeason(1)
                        .addEpisode(
                                new Episode("episode1",
                                        1,
                                        new Video("video4.mp4", 25, VideoResolution.FHD)
                                )
                        )
                        .addEpisode(
                                new Episode("episode2",
                                        2,
                                        new Video("video5.mp4", 25, VideoResolution.FHD)
                                )
                        )
                        .addEpisode(
                                new Episode("episode3",
                                        3,
                                        new Video("video6.mp4", 25, VideoResolution.FHD)
                                )
                        )
                        .build(),
                new TVSeries.TVSeriesBuilder("tvSeries3", VideoResolution.UHD)
                        .requiresSubscription()
                        .withDescription("Description of tvSeries3")
                        .withReleaseDate(LocalDate.now())
                        .withSeason(1)
                        .addEpisode(
                                new Episode(
                                        "episode1",
                                        1,
                                        new Video("video7.mp4", 25, VideoResolution.UHD)
                                )
                        )
                        .addEpisode(
                                new Episode(
                                        "episode2",
                                        2,
                                        new Video("video8.mp4", 25, VideoResolution.UHD)
                                )
                        )
                        .addEpisode(
                                new Episode(
                                        "episode3",
                                        3,
                                        new Video("video9.mp4", 25, VideoResolution.UHD)
                                )
                        )
                        .build()
        );
    }

}
