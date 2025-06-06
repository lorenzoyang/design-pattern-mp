package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidContentException;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TVSeriesTest {
    private List<Season> seasons;
    private TVSeries.TVSeriesBuilder defaultBuilder;

    @Before
    public void setUp() {
        this.seasons = List.of(
                new Season(1, List.of(
                        new Episode(1, 20),
                        new Episode(2, 20)
                )),
                new Season(2, List.of(
                        new Episode(1, 20),
                        new Episode(2, 20)
                ))
        );
        this.defaultBuilder = new TVSeries.TVSeriesBuilder("TVSeries", new Season(1, List.of()));
    }

    @Test
    public void testTVSeriesBuilderCreatesTVSeriesWithValidArguments() {
        LocalDate releaseDate = LocalDate.of(2025, 1, 1);
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries", seasons.get(0))
                .withDescription("TVSeries Description")
                .withReleaseDate(releaseDate)
                .withResolution(VideoResolution.FULL_HD_1080P)
                .withSeason(seasons.get(1))
                .build();

        assertEquals("TVSeries", tvSeries.getTitle());
        assertThat(tvSeries.getDescription()).contains("TVSeries Description");
        assertThat(tvSeries.getReleaseDate()).contains(releaseDate);
        assertThat(tvSeries.getResolution()).contains(VideoResolution.FULL_HD_1080P);
        assertEquals(2, tvSeries.getSeasonsCount());

        Season firstSeason = tvSeries.iterator().next();
        assertEquals(2, firstSeason.getEpisodesCount());

        assertThat(tvSeries).containsExactly(seasons.get(0), seasons.get(1));
        
        int expectedDuration = seasons.stream()
                .mapToInt(Season::getDurationInMinutes)
                .sum();
        assertEquals(expectedDuration, tvSeries.getDurationInMinutes());
    }

    @Test
    public void testTVSeriesBuilderConstructorThrowsNullPointerExceptionForNullTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder(null, new Season(1, List.of())))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Content title cannot be null");
    }

    @Test
    public void testTVSeriesBuilderConstructorThrowsInvalidContentExceptionForBlankTitle() {
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("    ", new Season(1, List.of())))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
        assertThatThrownBy(() -> new TVSeries.TVSeriesBuilder("", new Season(1, List.of())))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content title cannot be blank");
    }

    @Test
    public void testWithDescriptionThrowsInvalidContentExceptionForBlankDescription() {
        assertThatThrownBy(() -> defaultBuilder.withDescription("    "))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
        assertThatThrownBy(() -> defaultBuilder.withDescription(""))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("Content description cannot be blank");
    }

    @Test
    public void testWithSeasonThrowsInvalidContentExceptionForNonConsecutiveSeasonNumbers() {
        assertThatThrownBy(() -> defaultBuilder.withSeason(new Season(3, List.of())))
                .isInstanceOf(InvalidContentException.class)
                .hasMessage("TV series season numbers must be consecutive");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        TVSeries tvSeries = new TVSeries.TVSeriesBuilder("TVSeries", seasons.get(0))
                .build();

        int expected = seasons.get(0).getDurationInMinutes();
        assertEquals(expected, tvSeries.getDurationInMinutes());
    }

    @Test
    public void testEqualsReturnsTrueForSameTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries", seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries", seasons.get(0))
                .build();

        assertEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1", seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries2", seasons.get(0))
                .build();

        assertNotEquals(tvSeries1, tvSeries2);
    }

    @Test
    public void testHashCodeIsBasedOnTitle() {
        TVSeries tvSeries1 = new TVSeries.TVSeriesBuilder("TVSeries1", seasons.get(0))
                .build();
        TVSeries tvSeries2 = new TVSeries.TVSeriesBuilder("TVSeries1", seasons.get(0))
                .build();
        TVSeries tvSeries3 = new TVSeries.TVSeriesBuilder("TVSeries2", seasons.get(0))
                .build();

        assertEquals(tvSeries1.hashCode(), tvSeries2.hashCode());
        assertNotEquals(tvSeries1.hashCode(), tvSeries3.hashCode());
        assertNotEquals(tvSeries2.hashCode(), tvSeries3.hashCode());
    }
}