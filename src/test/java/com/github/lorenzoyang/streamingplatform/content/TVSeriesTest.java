package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.video.Video;
import com.github.lorenzoyang.streamingplatform.content.video.VideoResolution;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TVSeriesTest {
    private List<Episode> episodes;

    @Before
    public void setUp() {
        this.episodes = new ArrayList<>();
        episodes.add(new Episode("episode1", 1, new Video("video1", 120)));
        episodes.add(new Episode("episode2", 2, new Video("video2", 120)));
        episodes.add(new Episode("episode3", 3, new Video("video3", 120)));
    }

    @Test
    public void testTVSeriesBuilderThrowsInvalidContentExceptionForInvalidTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder(null, VideoResolution.HD))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");

        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("    ", VideoResolution.HD))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");
    }

    @Test
    public void testTVSeriesBuilderThrowsNullPointerExceptionForNullRequiredResolution() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("tvSeries1", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Required resolution cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForInvalidDescription() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD);

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");
    }

    @Test
    public void testWithReleaseDateThrowsInvalidContentExceptionForInvalidReleaseDate() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD);

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be null");

        assertThatThrownBy(() -> builder.withReleaseDate(LocalDate.now().plusDays(1)))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be in the future");
    }

    @Test
    public void testAddEpisodeRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .addEpisode(episodes.get(0))
                .addEpisode(episodes.get(1))
                .addEpisode(episodes.get(2))
                .build();

        assertThat(tvSeries.getEpisodes())
                .toIterable()
                .hasSize(episodes.size())
                .isEqualTo(episodes);
    }

    @Test
    public void testWithEpisodesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withEpisodes(episodes)
                .build();

        assertThat(tvSeries.getEpisodes())
                .toIterable()
                .hasSize(episodes.size())
                .isEqualTo(episodes);
    }

    @Test
    public void testWithSeasonThrowsIllegalArgumentExceptionForInvalidSeason() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD);

        assertThatThrownBy(() -> builder.withSeason(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season must be positive and non-zero");

        assertThatThrownBy(() -> builder.withSeason(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season must be positive and non-zero");
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidData() {
        var releaseDate = LocalDate.now();
        var anotherEpisode = new Episode("episode4", 4, new Video("video4", 120));
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .withSeason(1)
                .withEpisodes(episodes)
                .addEpisode(anotherEpisode)
                .build();

        assertThat(tvSeries.getTitle()).isEqualTo("tvSeries1");
        assertThat(tvSeries.getRequiredResolution()).isEqualTo(VideoResolution.HD);
        assertThat(tvSeries.getDescription()).isEqualTo("description");
        assertThat(tvSeries.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(tvSeries.getSeason()).isEqualTo(1);
        assertThat(tvSeries.isFree()).isFalse();

        episodes.add(anotherEpisode);
        assertThat(tvSeries.getEpisodes())
                .toIterable()
                .hasSize(4)
                .isEqualTo(episodes);
    }

    @Test
    public void testGetDurationMinutesRunsCorrectly() {
        double totalDurationMinutes = episodes.stream()
                .mapToDouble(episode -> episode.getVideo().getDurationMinutes())
                .sum();
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withEpisodes(episodes)
                .build();
        assertThat(tvSeries.getDurationMinutes()).isEqualTo(totalDurationMinutes);
    }

    @Test
    public void testPlayThrowsNullPointerExceptionForNullArguments() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withEpisodes(episodes)
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        assertThatThrownBy(() -> tvSeries.play(null, ViewingProgress.empty(), 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");

        assertThatThrownBy(() -> tvSeries.play(user, null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Current progress cannot be null");
    }

    @Test
    public void testPlayThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withEpisodes(episodes)
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        assertThatThrownBy(() -> tvSeries.play(user, ViewingProgress.empty(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch cannot be negative");
    }

    @Test
    public void testPlayThrowsAccessDeniedExceptionForNoAccessUser() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .requiresSubscription()
                .withEpisodes(episodes)
                .build();
        User user = new User.UserBuilder("user1", "password")
                .build();

        assertThatThrownBy(() -> tvSeries.play(user, ViewingProgress.empty(), 60))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(String.format("User %s does not have access to content '%s'.",
                        user.getUsername(), tvSeries.getTitle()));
    }

    @Test
    public void testPlayRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .requiresSubscription()
                .withEpisodes(episodes)
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        double timeToWatch = 240;
        ViewingProgress progress1 = tvSeries.play(user, ViewingProgress.empty(), timeToWatch);

        assertThat(progress1.getStartingVideo()).isEqualTo(episodes.get(0).getVideo());
        assertThat(progress1.getCurrentViewingDuration()).isEqualTo(timeToWatch);
        assertThat(progress1.getTotalViewingDuration()).isEqualTo(timeToWatch);
        assertThat(progress1.isCompleted(tvSeries)).isFalse();

        timeToWatch = 60;
        ViewingProgress progress2 = tvSeries.play(user, progress1, timeToWatch);

        assertThat(progress2.getStartingVideo()).isEqualTo(episodes.get(2).getVideo());
        assertThat(progress2.getCurrentViewingDuration()).isEqualTo(timeToWatch);
        assertThat(progress2.getTotalViewingDuration()).isEqualTo(progress1.getTotalViewingDuration() + timeToWatch);

    }

    @Test
    public void testEqualsReturnsTrueForSameTitleAndSeason() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withSeason(1)
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.SD)
                .withSeason(1)
                .build();

        assertThat(tvSeries1).isEqualTo(tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitleOrSeason() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withSeason(1)
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries2", VideoResolution.SD)
                .withSeason(1)
                .build();
        assertThat(tvSeries1).isNotEqualTo(tvSeries2);

        tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withSeason(1)
                .build();
        tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.SD)
                .withSeason(2)
                .build();
        assertThat(tvSeries1).isNotEqualTo(tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitleAndSeason() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withSeason(1)
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.SD)
                .withSeason(1)
                .build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("tvSeries1", VideoResolution.HD)
                .withSeason(2)
                .build();
        TVSeries tvSeries4 = new TVSeries.TVSeriesBuilder("tvSeries2", VideoResolution.HD)
                .withSeason(1)
                .build();

        assertThat(tvSeries1.hashCode()).isEqualTo(tvSeries2.hashCode());
        assertThat(tvSeries1.hashCode()).isNotEqualTo(tvSeries3.hashCode());
        assertThat(tvSeries1.hashCode()).isNotEqualTo(tvSeries4.hashCode());
        assertThat(tvSeries2.hashCode()).isNotEqualTo(tvSeries3.hashCode());
        assertThat(tvSeries2.hashCode()).isNotEqualTo(tvSeries4.hashCode());
    }
}