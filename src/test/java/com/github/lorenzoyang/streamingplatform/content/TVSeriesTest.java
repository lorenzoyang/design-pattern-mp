package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.User;
import com.github.lorenzoyang.streamingplatform.exceptions.AccessDeniedException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TVSeriesTest {
    private final double EPISODE_DURATION = 20;

    private LocalDate releaseDate;
    private List<Episode> episodes;

    @Before
    public void setUp() {
        this.releaseDate = LocalDate.of(2025, 1, 1);
        this.episodes = new ArrayList<>(List.of(
                new Episode("episode1", 1, EPISODE_DURATION),
                new Episode("episode2", 2, EPISODE_DURATION),
                new Episode("episode3", 3, EPISODE_DURATION)
        ));
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidArguments() {
        var episode1 = new Episode("episode1", episodes.size() + 1, EPISODE_DURATION);
        var episode2 = new Episode("episode2", 1, EPISODE_DURATION);

        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .addEpisodes(1, episodes)
                .addSingleEpisode(1, episode1)
                .addSeason(2)
                .addSingleEpisode(2, episode2)
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

        assertThatThrownBy(() -> builder.withReleaseDate(LocalDate.now().plusDays(10)))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Release date cannot be in the future");
    }

    @Test
    public void testAddSingleEpisodeThrowsIllegalArgumentExceptionForNonExistingSeason() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode("episode", 1, EPISODE_DURATION);

        assertThatThrownBy(() -> builder.addSingleEpisode(2, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season 2 does not exist");

        assertThatThrownBy(() -> builder.addSingleEpisode(3, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season 3 does not exist");
    }

    @Test
    public void testAddSingleEpisodeThrowsNullPointerExceptionForNullEpisode() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.addSingleEpisode(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testAddSingleEpisodeThrowsIllegalArgumentExceptionForExistingEpisode() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode("episode", 1, EPISODE_DURATION);

        builder.addSingleEpisode(1, episode);

        assertThatThrownBy(() -> builder.addSingleEpisode(1, episode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Episode already exists");
    }

    @Test
    public void testAddSingleEpisodeThrowsIllegalArgumentExceptionForInvalidEpisodeNumber() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode1 = new Episode("episode1", 1, EPISODE_DURATION);
        var episode2 = new Episode("episode2", 3, EPISODE_DURATION);

        builder.addSingleEpisode(1, episode1);

        assertThatThrownBy(() -> builder.addSingleEpisode(1, episode2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid episode number");
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
    public void testGetEpisodesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        assertThat(tvSeries.getEpisodes(1))
                .toIterable()
                .isEqualTo(episodes);
    }

    @Test
    public void testGetEpisodesThrowsIllegalArgumentExceptionForNonExistingSeason() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();

        assertThatThrownBy(() -> tvSeries.getEpisodes(2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Season 2 does not exist");
    }

    @Test
    public void testPlayRunsCorrectly() {
        var episode = new Episode("episode", 1, EPISODE_DURATION);
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .addSeason(2)
                .addSingleEpisode(2, episode)
                .requiresSubscription()
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        ViewingProgress progress = tvSeries.play(user, ViewingProgress.empty(), EPISODE_DURATION * 3);
        assertThat(progress.getStartingEpisode()).isEqualTo(episodes.get(0));
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(EPISODE_DURATION * 3);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(EPISODE_DURATION * 3);
        assertThat(progress.isCompleted(tvSeries)).isFalse();

        progress = tvSeries.play(user, progress, EPISODE_DURATION);
        assertThat(progress.getStartingEpisode()).isEqualTo(episode);
        assertThat(progress.getCurrentViewingDuration()).isEqualTo(EPISODE_DURATION);
        assertThat(progress.getTotalViewingDuration()).isEqualTo(EPISODE_DURATION * 4);
        assertThat(progress.isCompleted(tvSeries)).isTrue();
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