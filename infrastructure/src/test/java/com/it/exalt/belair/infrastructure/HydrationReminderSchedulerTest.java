package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HydrationReminderSchedulerTest {

    @Test
    void shouldSendReminderOnlyWhenDue() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-31T10:00:00Z"), ZoneId.of("Europe/Paris"));
        InMemoryNotificationGateway notificationGateway = new InMemoryNotificationGateway(clock);
        HydrationReminderScheduler scheduler = new HydrationReminderScheduler(notificationGateway, clock);
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));

        assertEquals(1, scheduler.sendDueReminders(List.of(alice), Map.of(alice, 2)));
        assertEquals(0, scheduler.sendDueReminders(List.of(alice), Map.of(alice, 2)));
        assertEquals(1, notificationGateway.sentNotifications().size());
    }

    @Test
    void shouldHonorWindowAndAlcoholFrequency() {
        MutableClock clock = new MutableClock(Instant.parse("2026-05-31T08:00:00Z"), ZoneId.of("Europe/Paris"));
        InMemoryNotificationGateway notificationGateway = new InMemoryNotificationGateway(clock);
        HydrationReminderScheduler scheduler = new HydrationReminderScheduler(notificationGateway, clock);
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));

        assertEquals(0, scheduler.sendDueReminders(List.of(alice), Map.of()));
        clock.advance(Duration.ofHours(1));
        assertEquals(1, scheduler.sendDueReminders(List.of(alice), Map.of(alice, 4)));
        clock.advance(Duration.ofMinutes(29));
        assertEquals(0, scheduler.sendDueReminders(List.of(alice), Map.of(alice, 4)));
        clock.advance(Duration.ofMinutes(1));
        assertEquals(1, scheduler.sendDueReminders(List.of(alice), Map.of(alice, 4)));
        assertEquals(2, notificationGateway.sentNotifications().size());
    }

    @Test
    void shouldValidateSchedulerDependenciesAndInputs() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-31T10:00:00Z"), ZoneId.of("Europe/Paris"));
        HydrationReminderScheduler scheduler = new HydrationReminderScheduler(new InMemoryNotificationGateway(clock), clock);

        assertThrows(NullPointerException.class, () -> new HydrationReminderScheduler(null, clock));
        assertThrows(NullPointerException.class, () -> new HydrationReminderScheduler(new InMemoryNotificationGateway(clock), null));
        assertThrows(NullPointerException.class, () -> scheduler.sendDueReminders(null, Map.of()));
    }

    private static final class MutableClock extends Clock {
        private Instant instant;
        private final ZoneId zone;

        private MutableClock(Instant instant, ZoneId zone) {
            this.instant = instant;
            this.zone = zone;
        }

        private void advance(Duration duration) {
            instant = instant.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return zone;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return new MutableClock(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
