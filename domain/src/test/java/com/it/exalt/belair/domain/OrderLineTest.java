package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class OrderLineTest {

    @Test
    void shouldCompareLinesByNameClassAndTypeButNotQuantity() {
        OrderLine oneBeer = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1);
        OrderLine twoBeers = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 2);
        OrderLine premiumBeer = new DrinkOrderLine("Beer", DrinkType.PREMIUM_ALCOHOLIC, 1);

        assertEquals(oneBeer, twoBeers);
        assertEquals(oneBeer.hashCode(), twoBeers.hashCode());
        assertNotEquals(oneBeer, premiumBeer);
        assertNotEquals(oneBeer, new FoodOrderLine("Beer", FoodType.SNACK, 1));
    }
}
