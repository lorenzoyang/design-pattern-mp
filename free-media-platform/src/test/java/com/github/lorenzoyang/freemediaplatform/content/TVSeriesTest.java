package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TVSeriesTest {
    private List<Season> seasons;
    private TVSeries.TVSeriesBuilder builder;

    @Before
    public void setUp() {
        this.seasons = Stream.of(
                new Season(1, List.of(
                        new Episode(1, 20),
                        new Episode(2, 20)
                )),
                new Season(2, List.of(
                        new Episode(1, 20),
                        new Episode(2, 20)
                ))
        ).collect(Collectors.toList());
        this.builder = new TVSeries.TVSeriesBuilder("TVSeries");
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidArguments() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        TVSeries tvSeries = builder
                .withDescription("TVSeries Description")
                .withReleaseDate(releaseDate)
                .withSeason(seasons.get(0))
                .withSeason(seasons.get(1))
                .build();

        assertEquals("TVSeries", tvSeries.getTitle());
        assertThat(tvSeries.getDescription()).contains("TVSeries Description");
        assertThat(tvSeries.getReleaseDate()).contains(releaseDate);
        assertEquals(2, tvSeries.getSeasonsCount());

        int expectedDuration = seasons.stream()
                .mapToInt(Season::getDurationInMinutes)
                .sum();
        assertEquals(expectedDuration, tvSeries.getDurationInMinutes());

        assertThat(tvSeries.getEpisodesIterator(1))
                .toIterable()
                .isEqualTo(seasons.get(0).getEpisodes());

        assertThat(tvSeries.getEpisodesIterator(2))
                .toIterable()
                .isEqualTo(seasons.get(1).getEpisodes());
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
    public void testWithSeasonThrowsInvalidContentExceptionForNonConsecutiveSeasonNumbers() {
        assertThatThrownBy(() -> builder.withSeason(new Season(2, List.of())))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("TV series season numbers must be consecutive");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        TVSeries tvSeries = builder
                .withSeason(seasons.get(0))
                .build();

        int expected = seasons.get(0).getDurationInMinutes();
        assertEquals(expected, tvSeries.getDurationInMinutes());
    }

    @Test
    public void testGetEpisodesIteratorRunsCorrectly() {
        TVSeries tvSeries = builder
                .withSeason(seasons.get(0))
                .build();

        assertThat(tvSeries.getEpisodesIterator(1))
                .toIterable()
                .isEqualTo(seasons.get(0).getEpisodes());
    }

    @Test
    public void testGetEpisodesIteratorThrowsInvalidContentExceptionForNonExistingSeason() {
        TVSeries tvSeries = builder
                .withSeason(seasons.get(0))
                .build();

        assertThatThrownBy(() -> tvSeries.getEpisodesIterator(2))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Invalid TV series season number");
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries")
                .withSeason(seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries")
                .withSeason(seasons.get(0))
                .build();

        assertEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1")
                .withSeason(seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries2")
                .withSeason(seasons.get(0))
                .build();

        assertNotEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1")
                .withSeason(seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries1")
                .withSeason(seasons.get(0))
                .build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("TVSeries2")
                .withSeason(seasons.get(0))
                .build();

        assertEquals(tvSeries1.hashCode(), tvSeries2.hashCode());
        assertNotEquals(tvSeries1.hashCode(), tvSeries3.hashCode());
        assertNotEquals(tvSeries2.hashCode(), tvSeries3.hashCode());
    }
}