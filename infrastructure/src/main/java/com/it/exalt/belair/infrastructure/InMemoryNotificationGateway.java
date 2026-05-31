package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryNotificationGateway implements NotificationGateway {
    private final Clock clock;
    private final List<Notification> notifications = new ArrayList<>();

    public InMemoryNotificationGateway(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    @Override
    public void sendTo(FestivalGoer recipient, String message) {
        notifications.add(new Notification(recipient, message, clock.instant()));
    }

    public List<Notification> sentNotifications() {
        return List.copyOf(notifications);
    }

    public void clear() {
        notifications.clear();
    }
}
