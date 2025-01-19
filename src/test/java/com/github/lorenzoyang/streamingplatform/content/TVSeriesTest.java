package com.github.lorenzoyang.streamingplatform.content;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidContentException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidEpisodeException;
import com.github.lorenzoyang.streamingplatform.exceptions.InvalidSeasonException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

public class TVSeriesTest {
    private List<Episode> episodes;
    private TVSeries.TVSeriesBuilder builder;

    @Before
    public void setUp() {
        this.episodes = new ArrayList<>(List.of(
                new Episode(1, 20),
                new Episode(2, 20),
                new Episode(3, 20)
        ));
        this.builder = new TVSeries.TVSeriesBuilder("TVSeries");
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidArguments() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        var newEpisode = new Episode(1, 20);
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .requiresSubscription()
                .withDescription("TVSeries Description")
                .withReleaseDate(releaseDate)
                .addEpisodes(1, episodes)
                .addSeason(2)
                .addSingleEpisode(2, newEpisode)
                .build();

        assertEquals("TVSeries", tvSeries.getTitle());
        assertTrue(tvSeries.isPremium());
        assertThat(tvSeries.getDescription()).contains("TVSeries Description");
        assertThat(tvSeries.getReleaseDate()).contains(releaseDate);
        assertEquals(2, tvSeries.getSeasonsCount());

        int expectedDuration = episodes.stream()
                .mapToInt(Episode::getDurationInMinutes)
                .sum() +
                newEpisode.getDurationInMinutes();
        assertEquals(expectedDuration, tvSeries.getDurationInMinutes());

        assertThat(tvSeries.getEpisodesIterator(1))
                .toIterable()
                .isEqualTo(episodes);
        assertThat(tvSeries.getEpisodesIterator(2))
                .toIterable()
                .isEqualTo(List.of(newEpisode));
    }

    @Test
    public void testTVSeriesBuilderConstructorThrowsNullPointerExceptionForNullTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content title cannot be null");
    }

    @Test
    public void testTVSeriesBuilderConstructorThrowsInvalidContentExceptionForBlankTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder(""))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
    }

    @Test
    public void testWithDescriptionThrowsNullPointerExceptionForNullDescription() {
        var builder = new TVSeries.TVSeriesBuilder("TVSeries");

        assertThatThrownBy(() -> builder.withDescription(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content description cannot be null");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForBlankDescription() {
        assertThatThrownBy(() -> builder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
        assertThatThrownBy(() -> builder.withDescription(""))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
    }

    @Test
    public void testWithReleaseDateThrowsNullPointerExceptionForNullReleaseDate() {
        assertThatThrownBy(() -> builder.withReleaseDate(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content release date cannot be null");
    }

    @Test
    public void testAddSeasonThrowsInvalidContentExceptionForNonConsecutiveSeasonNumber() {
        assertThatThrownBy(() -> builder.addSeason(3))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Season number must be consecutive");
    }

    @Test
    public void testAddSingleEpisodeThrowsInvalidSeasonExceptionForNonExistingSeason() {
        var episode = new Episode(1, 20);

        assertThatThrownBy(() -> builder.addSingleEpisode(2, episode))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Season 2 does not exist");
    }

    @Test
    public void testAddSingleEpisodeThrowsNullPointerExceptionForNullEpisode() {
        assertThatThrownBy(() -> builder.addSingleEpisode(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episode cannot be null");
    }

    @Test
    public void testAddSingleEpisodeThrowsInvalidEpisodeExceptionForNonConsecutiveEpisodeNumber() {
        builder.addSingleEpisode(1, new Episode(1, 20));
        assertThatThrownBy(
                () -> builder.addSingleEpisode(1, new Episode(1, 30))
        )
                .isInstanceOf(InvalidEpisodeException.class)
                .hasMessage("Episode number must be consecutive");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .addEpisodes(1, episodes)
                .build();

        int expected = episodes.stream()
                .mapToInt(Episode::getDurationInMinutes)
                .sum();
        assertEquals(expected, tvSeries.getDurationInMinutes());
    }

    @Test
    public void testGetEpisodesIteratorRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .addEpisodes(1, episodes)
                .build();

        assertThat(tvSeries.getEpisodesIterator(1))
                .toIterable()
                .isEqualTo(episodes);
    }

    @Test
    public void testGetEpisodesIteratorThrowsInvalidSeasonExceptionForNonExistingSeason() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries")
                .addEpisodes(1, episodes)
                .build();

        assertThatThrownBy(() -> tvSeries.getEpisodesIterator(2))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Season 2 does not exist");
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries").build();

        assertEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries2").build();

        assertNotEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1").build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries1").build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("TVSeries2").build();

        assertEquals(tvSeries1.hashCode(), tvSeries2.hashCode());
        assertNotEquals(tvSeries1.hashCode(), tvSeries3.hashCode());
        assertNotEquals(tvSeries2.hashCode(), tvSeries3.hashCode());
    }
}