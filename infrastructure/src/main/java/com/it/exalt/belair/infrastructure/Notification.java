package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;

import java.time.Instant;
import java.util.Objects;

public record Notification(FestivalGoer recipient, String message, Instant sentAt) {
    public Notification {
        Objects.requireNonNull(recipient, "recipient must not be null");
        Objects.requireNonNull(sentAt, "sentAt must not be null");
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        message = message.trim();
    }
}
