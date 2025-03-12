package com.github.lorenzoyang.freemediaplatform.content;

import com.github.lorenzoyang.freemediaplatform.exceptions.InvalidSeasonException;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SeasonTest {
    @Test
    public void testConstructorThrowsInvalidSeasonExceptionForInvalidSeasonNumber() {
        assertThatThrownBy(() -> new Season(0, List.of()))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Season number must be a positive integer greater than 0");

        assertThatThrownBy(() -> new Season(-1, List.of()))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Season number must be a positive integer greater than 0");
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullEpisodes() {
        assertThatThrownBy(() -> new Season(1, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Episodes cannot be null");
    }

    @Test
    public void testConstructorThrowsInvalidSeasonExceptionForEpisodesNotInOrder() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(3, 10);

        assertThatThrownBy(() -> new Season(1, List.of(episode1, episode2)))
                .isInstanceOf(InvalidSeasonException.class)
                .hasMessage("Episodes must be in order");
    }

    @Test
    public void testGetDurationInMinutesRunsCorrectly() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(2, 20);
        var season = new Season(1, List.of(episode1, episode2));

        int expected = episode1.getDurationInMinutes() + episode2.getDurationInMinutes();
        assertEquals(expected, season.getDurationInMinutes());
    }

    @Test
    public void testEqualsReturnsTrueForSameFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(2, 20);
        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(1, List.of(episode1, episode2));

        assertEquals(season1, season2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(2, 20);

        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(2, List.of(episode1, episode2));
        var season3 = new Season(1, List.of(episode1));

        assertNotEquals(season1, season2);
        assertNotEquals(season1, season3);
    }

    @Test
    public void testHashCodeIsBasedOnAllFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(2, 20);

        var season1 = new Season(1, List.of(episode1, episode2));
        var season2 = new Season(1, List.of(episode1, episode2));
        assertEquals(season1.hashCode(), season2.hashCode());

        var season3 = new Season(2, List.of(episode1, episode2));
        assertNotEquals(season1.hashCode(), season3.hashCode());

        var season4 = new Season(1, List.of(episode1));
        assertNotEquals(season1.hashCode(), season4.hashCode());
    }
}