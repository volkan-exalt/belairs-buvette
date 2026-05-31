package com.it.exalt.belair.infrastructure;

import com.it.exalt.belair.domain.Order;
import com.it.exalt.belair.domain.OrderChangeRequest;

import java.util.Objects;

public class OrderNotificationService {
    private final NotificationGateway notificationGateway;

    public OrderNotificationService(NotificationGateway notificationGateway) {
        this.notificationGateway = Objects.requireNonNull(notificationGateway, "notificationGateway must not be null");
    }

    public void notifyAcknowledged(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        notificationGateway.sendTo(
                order.owner(),
                "Votre commande est en preparation. Temps estime : "
                        + order.estimatedPreparationMinutes()
                        + " minute(s)."
        );
    }

    public void notifyReady(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        notificationGateway.sendTo(order.owner(), "Votre commande est prete, vous pouvez venir la recuperer.");
    }

    public void notifyCanceled(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        notificationGateway.sendTo(order.owner(), "Votre commande a bien ete annulee.");
    }

    public void notifyChangeAccepted(Order order) {
        Objects.requireNonNull(order, "order must not be null");
        notificationGateway.sendTo(
                order.owner(),
                "Votre demande de modification est acceptee. Nouveau temps estime : "
                        + order.estimatedPreparationMinutes()
                        + " minute(s)."
        );
    }

    public void notifyChangeRejected(Order order, OrderChangeRequest changeRequest) {
        Objects.requireNonNull(order, "order must not be null");
        Objects.requireNonNull(changeRequest, "changeRequest must not be null");
        notificationGateway.sendTo(order.owner(), "Votre demande de modification est refusee.");
    }
}
