package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryNotificationGatewayTest {

    @Test
    void shouldStoreAndClearNotifications() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-31T12:00:00Z"), ZoneId.of("Europe/Paris"));
        InMemoryNotificationGateway notificationGateway = new InMemoryNotificationGateway(clock);
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));

        notificationGateway.sendTo(alice, "  Bonjour  ");

        assertEquals(1, notificationGateway.sentNotifications().size());
        assertEquals("Bonjour", notificationGateway.sentNotifications().getFirst().message());
        assertEquals(clock.instant(), notificationGateway.sentNotifications().getFirst().sentAt());
        assertThrows(UnsupportedOperationException.class, () -> notificationGateway.sentNotifications().clear());

        notificationGateway.clear();
        assertTrue(notificationGateway.sentNotifications().isEmpty());
    }

    @Test
    void shouldValidateClock() {
        assertThrows(NullPointerException.class, () -> new InMemoryNotificationGateway(null));
    }
}
