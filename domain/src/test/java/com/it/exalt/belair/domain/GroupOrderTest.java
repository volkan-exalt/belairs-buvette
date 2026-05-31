package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GroupOrderTest {

    @Test
    void shouldCreateGroupOrderWithContributions() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(4, 4));
        FestivalGoer bob = new FestivalGoer("Bob", new TokenBalance(2, 2));
        OrderLine meal = new FoodOrderLine("Pizza", FoodType.MEAL, 1);
        OrderLine drink = new DrinkOrderLine("Cocktail", DrinkType.PREMIUM_ALCOHOLIC, 1);

        GroupOrder groupOrder = alice.placeGroupOrder(
                List.of(meal, drink),
                Map.of(alice, new TokenBalance(1, 2), bob, new TokenBalance(1, 1))
        );

        assertEquals(new TokenBalance(3, 2), alice.tokenBalance());
        assertEquals(new TokenBalance(1, 1), bob.tokenBalance());
        assertEquals(OrderStatus.CREATED, groupOrder.status());
    }

    @Test
    void shouldCancelAndRefundContributions() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(4, 4));
        GroupOrder groupOrder = alice.placeGroupOrder(
                List.of(new FoodOrderLine("Pizza", FoodType.MEAL, 1)),
                Map.of(alice, new TokenBalance(0, 3))
        );

        groupOrder.cancel();

        assertEquals(OrderStatus.CANCELED, groupOrder.status());
        assertEquals(new TokenBalance(4, 4), alice.tokenBalance());
    }

    @Test
    void shouldValidateGroupOrderContributions() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(4, 4));
        FestivalGoer bob = new FestivalGoer("Bob", new TokenBalance(1, 1));
        OrderLine meal = new FoodOrderLine("Pizza", FoodType.MEAL, 1);

        assertThrows(DomainValidationException.class, () -> alice.placeGroupOrder(List.of(meal), Map.of()));
        assertThrows(DomainValidationException.class, () -> alice.placeGroupOrder(
                List.of(meal),
                Map.of(alice, new TokenBalance(0, 1))
        ));
        assertThrows(DomainValidationException.class, () -> alice.placeGroupOrder(
                List.of(meal),
                Map.of(bob, new TokenBalance(0, 3))
        ));

        GroupOrder groupOrder = alice.placeGroupOrder(List.of(meal), Map.of(alice, new TokenBalance(0, 3)));
        assertThrows(UnsupportedOperationException.class, () -> groupOrder.contributions().clear());
    }
}
