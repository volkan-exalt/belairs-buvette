package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.HydrationReminderPolicy;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HydrationReminderScheduler {
    private static final String REMINDER_MESSAGE =
            "Pensez a boire de l'eau regulierement et profitez du festival de facon responsable !";

    private final NotificationGateway notificationGateway;
    private final Clock clock;
    private final Map<String, LocalDateTime> lastReminderByFestivalGoer = new HashMap<>();

    public HydrationReminderScheduler(NotificationGateway notificationGateway, Clock clock) {
        this.notificationGateway = Objects.requireNonNull(notificationGateway, "notificationGateway must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    public int sendDueReminders(
            Collection<FestivalGoer> festivalGoers,
            Map<FestivalGoer, Integer> alcoholicDrinksLastHour
    ) {
        Objects.requireNonNull(festivalGoers, "festivalGoers must not be null");
        Map<FestivalGoer, Integer> alcoholConsumption = alcoholicDrinksLastHour == null
                ? Map.of()
                : alcoholicDrinksLastHour;

        LocalDateTime now = LocalDateTime.now(clock);
        if (!HydrationReminderPolicy.isWithinReminderWindow(now.toLocalTime())) {
            return 0;
        }

        int sent = 0;
        for (FestivalGoer festivalGoer : festivalGoers) {
            if (isReminderDue(festivalGoer, alcoholConsumption.getOrDefault(festivalGoer, 0), now)) {
                notificationGateway.sendTo(festivalGoer, REMINDER_MESSAGE);
                lastReminderByFestivalGoer.put(festivalGoer.name(), now);
                sent++;
            }
        }
        return sent;
    }

    private boolean isReminderDue(FestivalGoer festivalGoer, int alcoholicDrinksLastHour, LocalDateTime now) {
        LocalDateTime lastReminder = lastReminderByFestivalGoer.get(festivalGoer.name());
        if (lastReminder == null) {
            return true;
        }
        Duration nextInterval = HydrationReminderPolicy.nextReminderInterval(
                lastReminder.toLocalTime(),
                alcoholicDrinksLastHour
        );
        return !lastReminder.plus(nextInterval).isAfter(now);
    }
}
