package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HydrationReminderPolicyTest {

    @Test
    void shouldComputeReminderWindowAndIntervals() {
        assertTrue(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(11, 0)));
        assertTrue(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(15, 30)));
        assertFalse(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(10, 59)));
        assertFalse(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(19, 0)));

        assertEquals(Duration.ofHours(1), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(13, 0), 2));
        assertEquals(Duration.ofMinutes(30), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(13, 0), 4));
        assertEquals(Duration.ofHours(1), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(10, 0), 0));
        assertEquals(Duration.ofHours(15), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(20, 0), 0));
        assertThrows(DomainValidationException.class, () -> HydrationReminderPolicy.nextReminderInterval(null, 0));
    }
}
