package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {

    @Test
    void shouldPlaceAcknowledgeAndCancelOrder() {
        FestivalGoer festivalGoer = new FestivalGoer("Alice", new TokenBalance(6, 9));
        Order order = festivalGoer.placeOrder(List.of(
                new DrinkOrderLine("Biere", DrinkType.NORMAL_ALCOHOLIC, 1),
                new FoodOrderLine("Burger", FoodType.MEAL, 1)
        ));

        assertEquals(new TokenBalance(5, 6), festivalGoer.tokenBalance());
        assertEquals(OrderStatus.CREATED, order.status());
        assertEquals(new TokenBalance(1, 3), order.totalCost());

        order.acknowledge();
        assertEquals(OrderStatus.ACKNOWLEDGED, order.status());
        assertThrows(DomainValidationException.class, order::cancel);
    }

    @Test
    void shouldCancelCreatedOrderAndRefundTokens() {
        FestivalGoer festivalGoer = new FestivalGoer("Bob", new TokenBalance(3, 4));
        Order order = festivalGoer.placeOrder(List.of(new FoodOrderLine("Popcorn", FoodType.SNACK, 1)));

        order.cancel();

        assertEquals(OrderStatus.CANCELED, order.status());
        assertEquals(new TokenBalance(3, 4), festivalGoer.tokenBalance());
    }

    @Test
    void shouldProtectLifecycleRulesAndMarkReady() {
        FestivalGoer festivalGoer = new FestivalGoer("Dana", new TokenBalance(4, 4));
        OrderLine beer = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));

        assertThrows(UnsupportedOperationException.class, () -> order.lines().add(beer));
        order.changeItems(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));
        assertEquals(new TokenBalance(0, 0), order.totalCost());
        order.acknowledge();

        assertThrows(DomainValidationException.class, order::acknowledge);
        assertThrows(DomainValidationException.class, () -> order.changeItems(List.of(beer)));
        assertThrows(DomainValidationException.class, () -> order.prepareItem(beer, 1));
        assertFalse(order.canMarkReady());

        OrderLine water = new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1);
        order.prepareItem(water, 5);
        assertTrue(order.canMarkReady());
        order.markReady();
        assertEquals(OrderStatus.READY, order.status());
    }

    @Test
    void shouldValidateOrderCreationAndPreparation() {
        FestivalGoer festivalGoer = new FestivalGoer("Eli", new TokenBalance(4, 4));

        assertThrows(DomainValidationException.class, () -> festivalGoer.placeOrder(List.of()));

        Order order = festivalGoer.placeOrder(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));
        assertThrows(DomainValidationException.class, () -> order.prepareItem(order.lines().getFirst(), 1));
        assertThrows(DomainValidationException.class, order::markReady);

        order.acknowledge();
        assertThrows(DomainValidationException.class, () -> order.prepareItem(order.lines().getFirst(), 0));
    }
}
