package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.DrinkOrderLine;
import com.it.exalt.belair.domain.DrinkType;
import com.it.exalt.belair.domain.FestivalGoer;
import com.it.exalt.belair.domain.Order;
import com.it.exalt.belair.domain.OrderChangeRequest;
import com.it.exalt.belair.domain.TokenBalance;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderNotificationServiceTest {

    @Test
    void shouldNotifyEveryOrderOutcome() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-31T12:00:00Z"), ZoneId.of("Europe/Paris"));
        InMemoryNotificationGateway notificationGateway = new InMemoryNotificationGateway(clock);
        OrderNotificationService notificationService = new OrderNotificationService(notificationGateway);
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));
        Order order = alice.placeOrder(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));

        order.acknowledge();
        notificationService.notifyAcknowledged(order);
        order.prepareItem(order.lines().getFirst(), 1);
        order.markReady();
        notificationService.notifyReady(order);
        notificationService.notifyCanceled(order);
        notificationService.notifyChangeAccepted(order);
        notificationService.notifyChangeRejected(order, new OrderChangeRequest(order, List.of(order.lines().getFirst())));

        List<Notification> notifications = notificationGateway.sentNotifications();
        assertSame(alice, notifications.getFirst().recipient());
        assertTrue(notifications.stream().anyMatch(notification -> notification.message().contains("Temps estime")));
        assertTrue(notifications.stream().anyMatch(notification -> notification.message().contains("prete")));
        assertTrue(notifications.stream().anyMatch(notification -> notification.message().contains("annulee")));
        assertTrue(notifications.stream().anyMatch(notification -> notification.message().contains("acceptee")));
        assertTrue(notifications.stream().anyMatch(notification -> notification.message().contains("refusee")));
    }

    @Test
    void shouldValidateDependenciesAndInputs() {
        Clock clock = Clock.fixed(Instant.parse("2026-05-31T12:00:00Z"), ZoneId.of("Europe/Paris"));
        OrderNotificationService service = new OrderNotificationService(new InMemoryNotificationGateway(clock));

        assertThrows(NullPointerException.class, () -> new OrderNotificationService(null));
        assertThrows(NullPointerException.class, () -> service.notifyAcknowledged(null));
        assertThrows(NullPointerException.class, () -> service.notifyReady(null));
        assertThrows(NullPointerException.class, () -> service.notifyCanceled(null));
        assertThrows(NullPointerException.class, () -> service.notifyChangeAccepted(null));
    }
}
