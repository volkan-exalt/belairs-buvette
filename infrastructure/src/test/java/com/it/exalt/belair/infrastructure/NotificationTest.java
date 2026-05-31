package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificationTest {

    @Test
    void shouldTrimMessageAndValidateFields() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(1, 1));
        Instant sentAt = Instant.parse("2026-05-31T12:00:00Z");
        Notification notification = new Notification(alice, "  Bonjour  ", sentAt);

        assertEquals(alice, notification.recipient());
        assertEquals("Bonjour", notification.message());
        assertEquals(sentAt, notification.sentAt());
        assertThrows(NullPointerException.class, () -> new Notification(null, "message", sentAt));
        assertThrows(NullPointerException.class, () -> new Notification(alice, "message", null));
        assertThrows(IllegalArgumentException.class, () -> new Notification(alice, " ", sentAt));
    }
}
