package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FestivalGoerTest {

    @Test
    void shouldTrimNameAndManageTokens() {
        FestivalGoer festivalGoer = new FestivalGoer(" Alice ", new TokenBalance(6, 9));

        assertEquals("Alice", festivalGoer.name());
        festivalGoer.deductTokens(new TokenBalance(1, 2));
        assertEquals(new TokenBalance(5, 7), festivalGoer.tokenBalance());
        festivalGoer.addTokens(new TokenBalance(1, 1));
        assertEquals(new TokenBalance(6, 8), festivalGoer.tokenBalance());
    }

    @Test
    void shouldPlaceOrderAndGroupOrder() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(6, 9));
        FestivalGoer bob = new FestivalGoer("Bob", new TokenBalance(2, 2));

        Order order = alice.placeOrder(List.of(new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1)));
        assertEquals(new TokenBalance(5, 9), alice.tokenBalance());
        assertEquals(OrderStatus.CREATED, order.status());

        GroupOrder groupOrder = alice.placeGroupOrder(
                List.of(new FoodOrderLine("Pizza", FoodType.MEAL, 1)),
                Map.of(alice, new TokenBalance(0, 2), bob, new TokenBalance(0, 1))
        );
        assertEquals(OrderStatus.CREATED, groupOrder.status());
    }

    @Test
    void shouldValidateFestivalGoer() {
        FestivalGoer festivalGoer = new FestivalGoer("Alice", new TokenBalance(1, 1));

        assertThrows(DomainValidationException.class, () -> new FestivalGoer(" ", new TokenBalance(0, 0)));
        assertThrows(NullPointerException.class, () -> new FestivalGoer("Alice", null));
        assertThrows(DomainValidationException.class, () -> festivalGoer.deductTokens(new TokenBalance(2, 0)));
        assertThrows(DomainValidationException.class, () -> festivalGoer.placeGroupOrder(
                List.of(new FoodOrderLine("Pizza", FoodType.MEAL, 1)),
                Map.of()
        ));
    }
}
