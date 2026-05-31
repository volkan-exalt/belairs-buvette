package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CancelOrderTest {

    @Test
    void shouldCancelCreatedOrderAndRefundTokens() {
        FestivalGoer festivalGoer = new FestivalGoer("Bob", new TokenBalance(3, 4));
        Order order = festivalGoer.placeOrder(List.of(new FoodOrderLine("Popcorn", FoodType.SNACK, 1)));

        order.cancel();

        assertEquals(OrderStatus.CANCELED, order.status());
        assertEquals(new TokenBalance(3, 4), festivalGoer.tokenBalance());
    }
}
