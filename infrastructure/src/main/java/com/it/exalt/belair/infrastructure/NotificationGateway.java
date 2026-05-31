package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.FestivalGoer;

public interface NotificationGateway {
    void sendTo(FestivalGoer recipient, String message);
}
