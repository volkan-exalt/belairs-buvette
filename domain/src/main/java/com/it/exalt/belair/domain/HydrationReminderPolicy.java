package com.it.exalt.belair.domain;

import java.time.Duration;
import java.time.LocalTime;

public final class HydrationReminderPolicy {

    private static final LocalTime START = LocalTime.of(11, 0);
    private static final LocalTime END = LocalTime.of(19, 0);

    private HydrationReminderPolicy() {
        // utility class
    }

    public static boolean isWithinReminderWindow(LocalTime time) {
        return !time.isBefore(START) && time.isBefore(END);
    }

    public static Duration nextReminderInterval(LocalTime time, int alcoholicDrinksLastHour) {
        if (time == null) {
            throw new DomainValidationException("Time must not be null");
        }
        if (time.isBefore(START)) {
            return Duration.between(time, START);
        }
        if (!isWithinReminderWindow(time)) {
            return Duration.ofDays(1).minus(Duration.between(START, time));
        }
        return alcoholicDrinksLastHour > 3 ? Duration.ofMinutes(30) : Duration.ofHours(1);
    }
}
