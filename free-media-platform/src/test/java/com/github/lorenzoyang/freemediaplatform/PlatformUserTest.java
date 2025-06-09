package com.github.lorenzoyang.freemediaplatform;

import com.github.lorenzoyang.freemediaplatform.content.Content;
import com.github.lorenzoyang.freemediaplatform.content.Episode;
import com.github.lorenzoyang.freemediaplatform.content.Movie;
import com.github.lorenzoyang.freemediaplatform.utils.MockEmailNotificationService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

public class PlatformUserTest {
    private StreamingPlatform platform;
    private MockEmailNotificationService emailNotificationService;

    @Before
    public void setUp() {
        this.platform = new StreamingPlatform("Streaming Platform",
                () -> List.of(new Movie.MovieBuilder("Movie", new Episode(1, 120))
                        .build())
        );
        this.emailNotificationService = new MockEmailNotificationService();
        PlatformUser user = new PlatformUser("lorenzoyang@gmail.com", this.emailNotificationService);
        this.platform.addObserver(user);
    }

    @Test
    public void testConstructorThrowsNullPointerExceptionForNullArguments() {
        assertThatThrownBy(() -> new PlatformUser(null, this.emailNotificationService))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Email cannot be null");

        assertThatThrownBy(() -> new PlatformUser("lorenzoyang@gmail.com", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("EmailNotificationService cannot be null");
    }

    @Test
    public void testConstructorThrowsIllegalArgumentExceptionForInvalidEmail() {
        assertThatThrownBy(() -> new PlatformUser("", this.emailNotificationService))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address");

        assertThatThrownBy(() -> new PlatformUser("invalid-email", this.emailNotificationService))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email address");
    }

    @Test
    public void testNotifyChangeForAddContentEvent() {
        Content contentToAdd = new Movie.MovieBuilder("contentToAdd", new Episode(1, 1))
                .build();
        this.platform.addContent(contentToAdd);

        String expected = "To: lorenzoyang@gmail.com\n" +
                "Subject: New Content Added\n" +
                "Message: New content added: contentToAdd. Check your email for details.";
        assertEquals(expected, this.emailNotificationService.getLastNotificationMessage());
    }

    @Test
    public void testNotifyChangeForUpdateContentEvent() {
        Content oldContent = this.platform.contentIterator().next();
        Content updatedContent = new Movie.MovieBuilder(oldContent.getTitle(),
                new Episode(1, 1))
                .withDescription("Updated description")
                .build();
        this.platform.updateContent(updatedContent);

        String expected = "To: lorenzoyang@gmail.com\n" +
                "Subject: Content Updated\n" +
                "Message: Content updated: Movie. Check your email for details.";
        assertEquals(expected, this.emailNotificationService.getLastNotificationMessage());

    }

    @Test
    public void testNotifyChangeForRemoveContentEvent() {
        Content contentToRemove = this.platform.contentIterator().next();
        this.platform.removeContent(contentToRemove);

        String expected = "";
        assertEquals(expected, this.emailNotificationService.getLastNotificationMessage());
    }
}