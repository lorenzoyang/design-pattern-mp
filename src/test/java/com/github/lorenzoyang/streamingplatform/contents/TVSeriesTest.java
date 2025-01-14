package com.github.lorenzoyang.streamingplatform.contents;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TVSeriesTest {
    private List<Episode> episodes;

    @Before
    public void setUp() {
        this.episodes = new ArrayList<>(List.of(
                new Episode(1, 20),
                new Episode(2, 20),
                new Episode(3, 20)
        ));
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidArguments() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        var episode1 = new Episode(episodes.size() + 1, 20);
        var episode2 = new Episode(1, 20);

        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .requiresSubscription()
                .withDescription("description")
                .withReleaseDate(releaseDate)
                .addEpisodes(1, episodes)
                .addSingleEpisode(1, episode1)
                .addSeason(2)
                .addSingleEpisode(2, episode2)
                .build();

        assertEquals("tvSeries1", tvSeries.getTitle());
        assertEquals("description", tvSeries.getDescription());
        assertEquals(releaseDate, tvSeries.getReleaseDate());
        assertThat(tvSeries.getSeasons()).hasSize(2);

        Season season1 = tvSeries.getSeasons().get(0);
        assertEquals(1, season1.getSeasonNumber());
        assertThat(season1.getEpisodes())
                .toIterable()
                .hasSize(episodes.size() + 1);
        episodes.add(episode1); // Because 'addSingleEpisode' adds the episode to the first season
        assertThat(season1.getEpisodes())
                .toIterable()
                .isEqualTo(episodes);

        Season season2 = tvSeries.getSeasons().get(1);
        assertEquals(2, season2.getSeasonNumber());
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
    public void testWithDescriptionThrowsNullPointerExceptionForNullDescription() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Description cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForBlankDescription() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Description cannot be blank");
    }

    @Test
    public void testWithReleaseDateThrowsNullPointerExceptionForNullReleaseDate() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");

        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Release date cannot be null");
    }

    @Test
    public void testAddSingleEpisodeThrowsInvalidContentExceptionForNonExistingSeason() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode(1, 20);

        assertThatThrownBy(() -> builder.addSingleEpisode(2, episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Season 2 does not exist");

        assertThatThrownBy(() -> builder.addSingleEpisode(3, episode))
                .isInstanceOf(InvalidContentException.class)
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
    public void testAddSingleEpisodeThrowsInvalidContentExceptionForExistingEpisode() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode = new Episode(1, 20);

        builder.addSingleEpisode(1, episode);

        assertThatThrownBy(() -> builder.addSingleEpisode(1, episode))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Episode already exists");
    }

    @Test
    public void testAddSingleEpisodeThrowsInvalidContentExceptionForNotConsecutiveEpisodeNumber() {
        var builder = new TVSeries.TVSeriesBuilder("tvSeries1");
        var episode1 = new Episode(1, 20);
        var episode2 = new Episode(3, 20);

        builder.addSingleEpisode(1, episode1);

        assertThatThrownBy(() -> builder.addSingleEpisode(1, episode2))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Episode number must be consecutive");
    }

    @Test
    public void testGetDurationMinutesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        int totalDuration = episodes.stream()
                .mapToInt(Episode::getDurationMinutes)
                .sum();

        assertEquals(totalDuration, tvSeries.getDurationMinutes());
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
    public void testGetEpisodesThrowsInvalidContentExceptionForNonExistingSeason() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();

        assertThatThrownBy(() -> tvSeries.getEpisodes(2))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Season 2 does not exist");
    }

    @Test
    public void testPlayRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .requiresSubscription()
                .addEpisodes(1, episodes)
                .build();
        User user = new User.UserBuilder("user1", "password")
                .subscribe()
                .build();

        String expected = "Playing TV series 'tvSeries1' for 60 minutes.\n" +
                "Watchable Episodes: Season 1: 1, 2, 3, \n";
        assertEquals(expected, tvSeries.play(user, 60));
    }

    @Test
    public void testPlayThrowsNullPointerExceptionForNullUser() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();

        assertThatThrownBy(() -> tvSeries.play(null, 60))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("User cannot be null");
    }

    @Test
    public void testPlayThrowsIllegalArgumentExceptionForNegativeTimeToWatch() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("tvSeries1")
                .addEpisodes(1, episodes)
                .build();
        User user = new User.UserBuilder("user1", "password").build();

        assertThatThrownBy(() -> tvSeries.play(user, -1))
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

        assertThatThrownBy(() -> tvSeries.play(user, 60))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessage(String.format("User %s does not have access to contents '%s'.",
                        user.getUsername(), tvSeries.getTitle()));
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1").build();

        assertEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries2").build();

        assertNotEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("tvSeries1").build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("tvSeries2").build();

        assertEquals(tvSeries1.hashCode(), tvSeries2.hashCode());
        assertNotEquals(tvSeries1.hashCode(), tvSeries3.hashCode());
        assertNotEquals(tvSeries2.hashCode(), tvSeries3.hashCode());
    }
}