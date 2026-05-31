package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderChangeRequestTest {

    @Test
    void shouldApproveWhenPreparedItemCanBeTransferred() {
        FestivalGoer festivalGoer = new FestivalGoer("Carla", new TokenBalance(2, 2));
        OrderLine beer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));
        order.acknowledge();
        order.prepareItem(beer, 1);

        OrderChangeRequest request = order.requestChange(List.of(new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1)));
        request.approve();

        assertTrue(request.isApproved());
        assertEquals(OrderStatus.ACKNOWLEDGED, order.status());
    }

    @Test
    void shouldRejectAndProtectCompletedRequests() {
        FestivalGoer festivalGoer = new FestivalGoer("Carla", new TokenBalance(3, 3));
        OrderLine beer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));
        order.acknowledge();
        OrderChangeRequest request = order.requestChange(List.of(new DrinkOrderLine("Another beer", DrinkType.NORMAL_ALCOHOLIC, 1)));

        assertThrows(DomainValidationException.class, request::approve);
        request.reject();

        assertTrue(request.isRejected());
        assertThrows(DomainValidationException.class, request::reject);
        assertThrows(DomainValidationException.class, request::approve);
    }

    @Test
    void shouldValidateChangeRequestCreation() {
        FestivalGoer festivalGoer = new FestivalGoer("Carla", new TokenBalance(3, 3));
        OrderLine beer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));

        assertThrows(DomainValidationException.class, () -> order.requestChange(List.of(beer)));
        order.acknowledge();
        assertThrows(DomainValidationException.class, () -> new OrderChangeRequest(order, List.of()));
    }
}
