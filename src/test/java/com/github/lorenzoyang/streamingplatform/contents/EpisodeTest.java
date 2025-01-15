package com.github.lorenzoyang.streamingplatform.contents;

import com.github.lorenzoyang.streamingplatform.exceptions.InvalidEpisodeException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EpisodeTest {
    @Test
    public void testConstructorThrowsInvalidEpisodeExceptionForInvalidEpisodeNumber() {
        assertThatThrownBy(() -> new Episode(-1, 10))
                .isInstanceOf(InvalidEpisodeException.class)
                .hasMessage("Episode number must be a positive integer greater than 0.");

        assertThatThrownBy(() -> new Episode(0, 10))
                .isInstanceOf(InvalidEpisodeException.class)
                .hasMessage("Episode number must be a positive integer greater than 0.");
    }

    @Test
    public void testConstructorThrowsInvalidEpisodeExceptionForInvalidDurationInMinutes() {
        assertThatThrownBy(() -> new Episode(1, -1))
                .isInstanceOf(InvalidEpisodeException.class)
                .hasMessage("Duration must be a positive integer greater than 0.");

        assertThatThrownBy(() -> new Episode(1, 0))
                .isInstanceOf(InvalidEpisodeException.class)
                .hasMessage("Duration must be a positive integer greater than 0.");
    }

    @Test
    public void testEqualsReturnsTrueForSameFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(1, 10);

        assertEquals(episode1, episode2);
    }

    @Test
    public void testEqualsReturnsFalseForDifferentFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(2, 10);
        var episode3 = new Episode(1, 20);

        assertNotEquals(episode1, episode2);
        assertNotEquals(episode1, episode3);
        assertNotEquals(episode2, episode3);
    }

    @Test
    public void testHashCodeIsBasedOnAllFields() {
        var episode1 = new Episode(1, 10);
        var episode2 = new Episode(1, 10);
        var episode3 = new Episode(2, 20);

        assertEquals(episode1.hashCode(), episode2.hashCode());
        assertNotEquals(episode1.hashCode(), episode3.hashCode());
        assertNotEquals(episode2.hashCode(), episode3.hashCode());
    }
}