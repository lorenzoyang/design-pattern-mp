package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.content.utils.Episode;
import com.github.lorenzoyang.streamingplatform.content.utils.Season;
import com.github.lorenzoyang.streamingplatform.content.utils.ViewingProgress;
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
        this.episodes = new ArrayList<>(List.of(
                new Episode("episode1", 1, 26),
                new Episode("episode2", 2, 26),
                new Episode("episode3", 3, 26)
        ));
    }

    @Test
    public void testTVSeriesBuilderConstructorThrowsInvalidContentExceptionForInvalidTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");

        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Title cannot be null or blank");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForInvalidDescription() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be null or blank");
    }

    @Test
    public void testWithReleaseDateThrowsInvalidContentExceptionForInvalidReleaseDate() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be null");

        assertThatThrownBy(() -> builder.withReleaseDate(LocalDate.now().plusDays(1)))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be in the future");
    }

    @Test
    public void testAddEpisodeThrowsIllegalArgumentExceptionForNonExistingSeason() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode("episode", 1, 26);

        assertThatThrownBy(() -> builder.addEpisode(2, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season 2 does not exist");

        assertThatThrownBy(() -> builder.addEpisode(3, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season 3 does not exist");
    }

    @Test
    public void testAddEpisodeThrowsNullPointerExceptionForNullEpisode() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.addEpisode(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testAddEpisodeThrowsIllegalArgumentExceptionForExistingEpisode() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode("episode", 1, 26);

        builder.addEpisode(1, episode);

        assertThatThrownBy(() -> builder.addEpisode(1, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode already exists");
    }

    @Test
    public void testAddEpisodeThrowsIllegalArgumentExceptionForInvalidEpisodeNumber() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode1 = new Episode("episode1", 1, 26);
        var episode2 = new Episode("episode2", 3, 26);

        builder.addEpisode(1, episode1);

        assertThatThrownBy(() -> builder.addEpisode(1, episode2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid episode number");
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidData() {
        var episode1 = new Episode("episode1", episodes.size() + 1, 26);
        var episode2 = new Episode("episode2", 1, 26);
        var releaseDate = LocalDate.now();

        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .addEpisodes(1, episodes)
                .addEpisode(1, episode1)
                .addSeason(2)
                .addEpisode(2, episode2)
                .build();

        assertThat(tvSeries.getTitle()).isEqualTo("tvSeries1");
        assertThat(tvSeries.getDescription()).isEqualTo("description");
        assertThat(tvSeries.getReleaseDate()).isEqualTo(releaseDate);
        assertThat(tvSeries.getSeasons()).hasSize(2);

        Season season1 = tvSeries.getSeasons().get(0);
        assertThat(season1.getSeasonNumber()).isEqualTo(1);
        assertThat(season1.getEpisodes())
                .toIterable()
                .hasSize(episodes.size() + 1);

        episodes.add(episode1);
        assertThat(season1.getEpisodes())
                .toIterable()
                .isEqualTo(episodes);

        Season season2 = tvSeries.getSeasons().get(1);
        assertThat(season2.getSeasonNumber()).isEqualTo(2);
        assertThat(season2.getEpisodes())
                .toIterable()
                .containsExactly(episode2);
        assertThat(season2.getEpisodes())
                .toIterable()
                .isEqualTo(List.of(episode2));

    }

    @Test
    public void testGetDurationMinutesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        double totalDuration = episodes.stream()
                .mapToDouble(Episode::getDurationMinutes)
                .sum();
        assertThat(tvSeries.getDurationMinutes()).isEqualTo(totalDuration);
    }

    @Test
    public void testPlayThrowsNullPointerExceptionForNullArguments() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> tvSeries.play(null, ViewingProgress.empty(), 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");

        assertThatThrownBy(() -> tvSeries.play(user, null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Current progress cannot be null");
    }

    @Test
    public void testPlayThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> tvSeries.play(user, ViewingProgress.empty(), -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Time to watch cannot be negative");
    }

    @Test
    public void testPlayThrowsAccessDeniedExceptionForNoAccessUser() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> tvSeries.play(user, ViewingProgress.empty(), 60))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(String.format("User %s does not have access to content '%s'.",
                        user.getUsername(), tvSeries.getTitle()));
    }

    @Test
    public void testPlayRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        ViewingProgress progress = tvSeries.play(user, ViewingProgress.empty(), 60);

        assertThat(progress.getStartingEpisode()).isEqualTo(episodes.get(0));
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(60);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(60);
        assertThat(progress.isCompleted(tvSeries)).isFalse();
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1").build();

        assertThat(tvSeries1).isEqualTo(tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries2").build();

        assertThat(tvSeries1).isNotEqualTo(tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("tvSeries2").build();

        assertThat(tvSeries1.hashCode()).isEqualTo(tvSeries2.hashCode());
        assertThat(tvSeries1.hashCode()).isNotEqualTo(tvSeries3.hashCode());
        assertThat(tvSeries2.hashCode()).isNotEqualTo(tvSeries3.hashCode());
    }
}